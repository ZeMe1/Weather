package kz.zeme.weather.core.architecture

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.SharedFlow

/**
 * Исполнитель, отвечающий за обработку намерений и создание сообщений
 */
interface Middleware<in Intent : Any,  Action : Any, out Message : Any, out Label : Any> {
    /**
     * Поток одноразовых событий
     */
    val labels: SharedFlow<Label>

    /**
     * Поток сообщений для обновления состояния
     */
    val messages: SharedFlow<Message>

    /**
     * Выполняет намерение пользователя
     */
    @MainThread
    fun handleIntent(intent: Intent)

    /**
     * Called by the [BaseViewModel] for every `Action` produced by the [Bootstrapper]
     */
    @MainThread
    fun handleAction(action: Action)
    /**
     * Очищает ресурсы
     */
    @MainThread
    fun clear()
}