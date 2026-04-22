package kz.zeme.weather.core.architecture

import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext

/**
 * Используется для начальной инициализации [BaseViewModel].
 * Отправляет начальные `Actions`, подписывается на источники данных или выполняет любые другие начальные действия.
 * Все отправленные `Actions` будут переданы в [Middleware] через метод [Middleware.handleAction].
 *
 * @param Action тип Action
 *
 * @see BaseViewModel
 * @see Middleware
 */
interface Bootstrapper<out Action : Any> {

    /**
     * Инициализирует [Bootstrapper], вызывается внутренне [BaseViewModel].
     *
     * @param actionConsumer потребитель Actions, который будет использоваться Bootstrapper'ом,
     * должен быть вызван в главном потоке
     */
    @MainThread
    fun init(actionConsumer: (Action) -> Unit)

    /**
     * Вызывается [Store] в какой-то момент во время инициализации.
     */
    @MainThread
    operator fun invoke()

    /**
     * Освобождает ресурсы [Bootstrapper], вызывается [BaseViewModel] при завершении его работы.
     */
    @MainThread
    fun dispose()
}

/**
 * Абстрактная реализация [Bootstrapper], которая предоставляет [CoroutineScope] для запуска корутин.
 *
 * @param mainContext [CoroutineContext], который будет использоваться для [CoroutineScope]
 */
abstract class CoroutineBootstrapper<Action : Any>(mainContext: CoroutineContext = Dispatchers.Main) : Bootstrapper<Action> {

    // Поток для отправки Actions
    private val actionFlow = MutableSharedFlow<Action>()

    /**
     * [CoroutineScope], который могут использовать наследники [CoroutineBootstrapper] для запуска корутин.
     * [CoroutineScope] автоматически отменяется при вызове метода `dispose`.
     */
    protected val scope: CoroutineScope = CoroutineScope(mainContext)

    final override fun init(actionConsumer: (Action) -> Unit) {
        scope.launch { actionFlow.collect { actionConsumer(it) } }
    }

    /**
     * Отправляет `Action` в [Store].
     *
     * @param action `Action`, который нужно отправить.
     */
    protected fun dispatch(action: Action) {
        scope.launch { actionFlow.emit(action) }
    }

    override fun dispose() {
        scope.cancel()
    }
}

/**
 * Простая реализация [Bootstrapper].
 * Принимает массив `Actions` и отправляет их один за другим.
 *
 * @param actions массив `Actions`, которые будут отправлены
 */
@OptIn(ExperimentalAtomicApi::class)
class SimpleBootstrapper<out Action : Any>(private vararg val actions: Action) : Bootstrapper<Action> {

    @OptIn(ExperimentalAtomicApi::class)
    private val actionConsumer = AtomicReference<((Action) -> Unit)?>(null)

    @OptIn(ExperimentalAtomicApi::class)
    override fun init(actionConsumer: (Action) -> Unit) {
        this.actionConsumer.initialize(actionConsumer)
    }

    override fun invoke() {
        actions.forEach(actionConsumer.requireValue()::invoke)
    }

    override fun dispose() {
        // ничего не делаем (no-op)
    }
}