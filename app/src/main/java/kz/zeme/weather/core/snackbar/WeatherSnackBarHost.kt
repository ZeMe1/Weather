package kz.zeme.weather.core.snackbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapTo
import kotlinx.coroutines.delay

@Composable
fun WeatherSnackBarHost(
    hostState: WeatherSnackBarHostState,
    modifier: Modifier = Modifier,
    snackBar: @Composable (WeatherSnackBarData) -> Unit = { WeatherSnackBar(it) }
) {
    val currentData = hostState.currentSnackBarData
    val a11y = LocalAccessibilityManager.current

    LaunchedEffect(currentData) {
        if (currentData != null) {
            delay(currentData.visuals.duration.toMillis(a11y))
            currentData.dismiss()
        }
    }

    FadeInFadeOutWithScale(
        current  = currentData,
        modifier = modifier,
        content  = snackBar
    )
}

@Stable
interface WeatherSnackBarVisuals {
    val message: MessageText
    val actionLabel: String?
    val withDismissAction: Boolean
    val duration: WeatherSnackBarDuration

    @Stable
    sealed class MessageText {
        @Immutable
        data class Res(val res: Int) : MessageText()
        @Immutable
        data class Value(val text: String) : MessageText()

        @Composable
        fun asString(): String = when (this) {
            is Res   -> stringResource(res)
            is Value -> text
        }
    }
}

@Stable
interface WeatherSnackBarData {
    val visuals: WeatherSnackBarVisuals
    fun dismiss()
}

sealed interface WeatherSnackBarDuration {
    data object Short     : WeatherSnackBarDuration
    data object Long      : WeatherSnackBarDuration
    data object Indefinite: WeatherSnackBarDuration
    data class  Custom(val millis: Long) : WeatherSnackBarDuration
}

internal fun WeatherSnackBarDuration.toMillis(accessibilityManager: AccessibilityManager?): Long {
    val raw: Long = when (this) {
        is WeatherSnackBarDuration.Short      -> 3000L
        is WeatherSnackBarDuration.Long       -> 8000L
        is WeatherSnackBarDuration.Indefinite -> Long.MAX_VALUE
        is WeatherSnackBarDuration.Custom     -> millis
    } as Long
    return accessibilityManager?.calculateRecommendedTimeoutMillis(
        raw, containsIcons = false, containsText = true, containsControls = false
    ) ?: raw
}


enum class WeatherSnackBarResult { Dismissed }

@Composable
private fun FadeInFadeOutWithScale(
    current: WeatherSnackBarData?,
    modifier: Modifier = Modifier,
    content: @Composable (WeatherSnackBarData) -> Unit
) {
    val state = remember { FadeInFadeOutState<WeatherSnackBarData?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.fastMap { it.key }.toMutableList()
        if (!keys.contains(current)) keys.add(current)
        state.items.clear()
        keys.fastFilterNotNull().fastMapTo(state.items) { key ->
            FadeInFadeOutAnimationItem(key) { children ->
                val visible = key == current
                val duration = if (visible) SnackbarFadeInMillis else SnackbarFadeOutMillis
                val animDelay = if (visible && keys.fastFilterNotNull().size != 1)
                    SnackbarFadeOutMillis + SnackbarInBetweenDelayMillis else 0
                val opacity = animatedOpacity(tween(durationMillis = duration, delayMillis = animDelay, easing = LinearEasing), visible) {
                    if (key != state.current) {
                        state.items.removeAll { it.key == key }
                        state.scope?.invalidate()
                    }
                }
                val scale = animatedScale(tween(durationMillis = duration, delayMillis = animDelay, easing = FastOutSlowInEasing), visible)
                Box(
                    Modifier
                        .graphicsLayer(scaleX = scale.value, scaleY = scale.value, alpha = opacity.value)
                        .semantics { liveRegion = LiveRegionMode.Polite; dismiss { key.dismiss(); true } }
                ) { children() }
            }
        }
    }
    Box(modifier.padding(top = 30.dp).fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) -> key(item) { opacity { content(item!!) } } }
    }
}

private class FadeInFadeOutState<T> {
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}
private data class FadeInFadeOutAnimationItem<T>(val key: T, val transition: FadeInFadeOutTransition)
private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable private fun animatedOpacity(animation: AnimationSpec<Float>, visible: Boolean, onFinish: () -> Unit = {}): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    LaunchedEffect(visible) { alpha.animateTo(if (visible) 1f else 0f, animation); onFinish() }
    return alpha.asState()
}
@Composable private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
    LaunchedEffect(visible) { scale.animateTo(if (visible) 1f else 0.8f, animation) }
    return scale.asState()
}
private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0