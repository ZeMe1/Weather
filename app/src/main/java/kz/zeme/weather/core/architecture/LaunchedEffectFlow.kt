package kz.zeme.weather.core.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@[Composable NonRestartableComposable]
fun <T> LaunchedEffectSnapshotFlow(key: Any = Unit, snapshot: () -> T, block: suspend CoroutineScope.(T) -> Unit) {
    LaunchedEffect(key) { snapshotFlow(snapshot).collect { data -> block.invoke(this, data) } }
}

@[Composable NonRestartableComposable]
fun <T> LaunchedEffectFlow(key: Any = Unit, flow: () -> Flow<T>, block: suspend CoroutineScope.(T) -> Unit) {
    LaunchedEffect(key) { flow.invoke().collect { data -> block.invoke(this, data) } }
}

@[Composable NonRestartableComposable]
fun LaunchedEffectLifecycle(
    key: Any = Unit,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key, lifecycleOwner, lifecycleState) {
        lifecycleOwner.repeatOnLifecycle(state = lifecycleState) {
            block()
        }
    }
}

@Composable
fun <T> rememberMutableUpdatedState(newValue: T): MutableState<T> = remember { mutableStateOf(newValue) }.apply { value = newValue }

@Composable
fun <T> rememberUpdatedStateWithLifecycle(lifecycleState: Lifecycle.State, newValue: () -> T): MutableState<T> {
    val state = rememberMutableUpdatedState(newValue())
    LaunchedEffectLifecycle(lifecycleState = lifecycleState) { state.value = newValue() }
    return state
}
