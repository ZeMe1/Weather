package kz.zeme.weather.core.architecture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi


/**
 * Базовый ViewModel для MVI архитектуры.
 *
 * @param State тип состояния, представляющий UI состояние
 * @param Intent тип намерения, представляющий действия пользователя
 * @param Message тип сообщения, представляющий внутренние события
 * @param Label тип метки, представляющий одноразовые события (навигация, сообщения и т.д.)
 */
abstract class BaseViewModel<State : Any, Intent : Any, Action : Any, Message : Any, Label : Any> : ViewModel, StateHolder<State> {

    protected abstract val reducer: Reducer<State, Message>
    protected abstract val middleware: Middleware<Intent, Action, Message, Label>

    @OptIn(ExperimentalAtomicApi::class)
    private val isInitialized = AtomicBoolean(false)
    private val intentBuffer = mutableListOf<Intent>()

    /**
     * Обработчик ошибок, по умолчанию логирует ошибки
     */
    protected open val errorHandler: ErrorHandler = DefaultErrorHandler()
    protected abstract val bootstrapper: Bootstrapper<@UnsafeVariance Action>

    /**
     * Поток одноразовых событий (например, для навигации или показа сообщений)
     */
    val labels: SharedFlow<Label> by lazy { middleware.labels }

    @OptIn(ExperimentalAtomicApi::class)
    constructor() {
        viewModelScope.launch(Dispatchers.Main) {
            middleware.messages
                .onEach { message -> reduce { reducer.reduce(this, message) } }
                .launchIn(this)

            bootstrapper.init { catchingWithHandlerError { middleware.handleAction(it) } }
            bootstrapper.invoke()

            isInitialized.store(true)
            drainIntentBuffer()
        }
    }

    protected fun savedStateFlow(initial: State, handler: SavedStateHandle, strategy: SaveStateStrategy<State>): MutableStateFlow<State> {
        return MutableStateFlow(strategy.restoreState(handler, initial)).apply {
            viewModelScope.launch { strategy.observeAndSave(this@apply, handler, errorHandler) }
        }
    }

    private fun drainIntentBuffer() {
        intentBuffer.forEach { catchingWithHandlerError { middleware.handleIntent(it) } }
        intentBuffer.clear()
    }

    /**
     * Обрабатывает intent пользователя
     */
    @OptIn(ExperimentalAtomicApi::class)
    fun acceptIntent(intent: Intent) {
        if (isInitialized.load()) catchingWithHandlerError { middleware.handleIntent(intent) } else  intentBuffer.add(intent)
    }

    fun <T> catchingWithHandlerError(call: () -> T): Result<T> {
        return runCatching { call.invoke() }.onFailure { errorHandler.handleError(it) }
    }

    override fun onCleared() {
        super.onCleared()
        bootstrapper.dispose()
    }
}