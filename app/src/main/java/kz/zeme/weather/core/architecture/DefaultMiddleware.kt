package kz.zeme.weather.core.architecture

import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class DefaultMiddleware<in Intent : Any, Action : Any, in State : Any, Message : Any, Label : Any>(
    private val state: () -> State,
    protected val scope: CoroutineScope
) : Middleware<Intent, Action, Message, Label> {

    private val _labels = MutableSharedFlow<Label>()
    private val _messages = MutableSharedFlow<Message>()

    override val labels: SharedFlow<Label> get() = _labels
    override val messages: SharedFlow<Message> get() = _messages

    final override fun handleIntent(intent: Intent) {
        handleIntent(intent, state)
    }

    protected open fun handleIntent(intent: Intent, state: () -> State): Any {
        return Unit
    }

    final override fun handleAction(action: Action) {
        handleAction(action, state)
    }

    /**
     * Called by the [BaseViewModel] for every `Action` produced by the [Bootstrapper]
     *
     * @param action an `Action` produced by the [Bootstrapper]
     * @param getState a function that returns the current `State` of the [BaseViewModel], must be called on Main thread
     */
    @MainThread
    protected open fun handleAction(action: Action, @MainThread getState: () -> State) {
    }

    /**
     * Отправляет сообщение для обновления состояния
     */
    @MainThread
    protected fun dispatch(vararg messages: Message) {
        scope.launch { messages.forEach { _messages.emit(it) } }
    }

    /**
     * Отправляет одноразовое событие в View
     */
    @MainThread
    protected fun publish(vararg label: Label) {
        scope.launch { label.forEach { _labels.emit(it) } }
    }

    /**
     * Очищает ресурсы
     */
    @MainThread
    override fun clear() {
        scope.cancel()
    }
}