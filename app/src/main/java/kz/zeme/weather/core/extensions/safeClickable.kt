package kz.zeme.weather.core.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

private val DEFAULT_INTERVAL: Duration = 800.milliseconds

fun Modifier.safeClickable(
    enabled: Boolean = true,
    clickInterval: Duration = DEFAULT_INTERVAL,
    indication: Indication? = null,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val interval = if (clickInterval.isNegative()) Duration.ZERO else clickInterval

    var lastClick: TimeMark? by remember { mutableStateOf(null) }

    clickable(
        enabled = enabled,
        indication = indication,
        interactionSource = source
    ) {
        val canClick = lastClick?.elapsedNow()?.let { it >= interval } ?: true
        if (canClick) {
            lastClick = TimeSource.Monotonic.markNow()
            onClick()
        }
    }
}
