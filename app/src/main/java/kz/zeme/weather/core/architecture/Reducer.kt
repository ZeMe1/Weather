package kz.zeme.weather.core.architecture

import androidx.annotation.MainThread

/**
 * Редуктор, отвечающий за преобразование состояния на основе сообщения
 */
fun interface Reducer<State, in Message> {
    /**
     * Принимает текущее состояние и сообщение, возвращает новое состояние
     * Должен быть чистой функцией без побочных эффектов
     */
    @MainThread
    fun reduce(state: State, message: Message): State
}