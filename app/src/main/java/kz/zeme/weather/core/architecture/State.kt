package kz.zeme.weather.core.architecture

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Serializable
sealed class State<out T> {
    @Serializable data object Initial : State<Nothing>()
    @Serializable data object Loading : State<Nothing>()
    @Serializable data class Failure(val cause: @Contextual Exception) : State<Nothing>()
    @Serializable data class Success<T>(val data: T) : State<T>()
}

fun <T> State<T>.toResult(): Result<T> {
    return kotlin.runCatching { this as State.Success<T> }.map { it.data }
}

fun <T> Result<T>.toAbstractState(): State<T> {
    return fold({ State.Success(it) }, { State.Failure(Exception(it)) })
}

@OptIn(ExperimentalContracts::class)
inline fun <R, T> State<T>.map(transform: (value: T) -> R): State<R> {
    contract { callsInPlace(transform, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is State.Success<T> -> State.Success(transform(data))
        is State.Initial, is State.Loading, is State.Failure -> this
    }
}

/**
 * @return возвращает данные из [State.Success] или null
 */
fun <T : Any> State<T>.getOrNull(): T? {
    return (this as? State.Success<T>)?.data
}

fun <T : Any> State<T>.getOrThrow(): T {
    return (this as State.Success<T>).data
}

/**
 * @return возвращает данные из [State.Success] или defaultValue
 */
fun <T : Any> State<T>.getOrDefault(defaultValue: T): T {
    return getOrNull() ?: defaultValue
}