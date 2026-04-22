package kz.zeme.weather.core.architecture

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
fun <T : Any> AtomicReference<T?>.initialize(value: T) {
    if (!compareAndSet(expectedValue = null, newValue = value)) {
        error("Value is already initialized")
    }
}

@OptIn(ExperimentalAtomicApi::class)
fun <T : Any> AtomicReference<T?>.requireValue(): T =
    load() ?: error("Value was not initialized")