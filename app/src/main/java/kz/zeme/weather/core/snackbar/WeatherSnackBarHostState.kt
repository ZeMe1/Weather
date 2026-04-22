package kz.zeme.weather.core.snackbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

@Stable
class WeatherSnackBarHostState {

    private val mutex = Mutex()

    var currentSnackBarData by mutableStateOf<WeatherSnackBarData?>(null)
        private set

    /** Show a plain-text snackbar. */
    suspend fun show(
        message: String,
        duration: WeatherSnackBarDuration = WeatherSnackBarDuration.Short
    ): WeatherSnackBarResult = show(
        WeatherSnackBarVisualsImpl(
            WeatherSnackBarVisuals.MessageText.Value(message),
            duration = duration
        )
    )

    /** Show a string-resource snackbar. */
    suspend fun show(
        messageRes: Int,
        duration: WeatherSnackBarDuration = WeatherSnackBarDuration.Short
    ): WeatherSnackBarResult = show(
        WeatherSnackBarVisualsImpl(
            WeatherSnackBarVisuals.MessageText.Res(messageRes),
            duration = duration
        )
    )

    private suspend fun show(visuals: WeatherSnackBarVisuals): WeatherSnackBarResult =
        mutex.withLock {
            try {
                suspendCancellableCoroutine { cont ->
                    currentSnackBarData = WeatherSnackBarDataImpl(visuals, cont)
                }
            } finally {
                currentSnackBarData = null
            }
        }


    private data class WeatherSnackBarVisualsImpl(
        override val message: WeatherSnackBarVisuals.MessageText,
        override val actionLabel: String?        = null,
        override val withDismissAction: Boolean  = false,
        override val duration: WeatherSnackBarDuration
    ) : WeatherSnackBarVisuals

    private data class WeatherSnackBarDataImpl(
        override val visuals: WeatherSnackBarVisuals,
        private  val continuation: CancellableContinuation<WeatherSnackBarResult>
    ) : WeatherSnackBarData {
        override fun dismiss() {
            if (continuation.isActive) continuation.resume(WeatherSnackBarResult.Dismissed)
        }
    }
}