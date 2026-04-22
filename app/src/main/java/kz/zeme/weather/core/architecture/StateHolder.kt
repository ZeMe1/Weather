package kz.zeme.weather.core.architecture

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kz.zeme.weather.core.utils.castActionOrNull
import kz.zeme.weather.core.utils.castOrNull
import kz.zeme.weather.core.utils.castOrThrow

interface StateHolder<State> {

    /**
     * The implementation must be Mutable, or use explicit backing field (K2 feature)
     *
     * or you can override [reduce]
     */
    val states: StateFlow<State>
    val state: State get() = states.value

    /**
     * Обновляет текущее состояние, применяя [block] к его текущему значению.
     *
     * @throws IllegalStateException если [state] не является `MutableStateFlow`.
     */
    @MainThread
    fun reduce(block: State.() -> State) {
        require(states is MutableStateFlow) { "$state is not mutable. Use MutableStateFlow<State> in implementation." }
        castOrThrow<MutableStateFlow<State>>(states).update(block)
    }

    @MainThread
    fun <T : State> reduceIfInstance(block: T.() -> T) {
        castActionOrNull<T>(states.value) { reduce { block() } }
    }

    companion object {
        inline fun <State, reified T : State> StateHolder<State>.generateOrElse(
            fallback: () -> State = { state },
            crossinline block: T.() -> State
        ): State {
            return castOrNull<T>(states.value)?.let { block(it) } ?: fallback()
        }
    }
}