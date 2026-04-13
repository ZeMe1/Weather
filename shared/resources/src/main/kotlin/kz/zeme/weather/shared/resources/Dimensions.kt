package kz.zeme.weather.shared.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val smallPadding: Dp = 8.dp,
    val mediumPadding: Dp = 16.dp,
    val largePadding: Dp = 24.dp,
    val extraLargePadding: Dp = 32.dp,

    val horizontalScreenPadding: Dp = 16.dp,
    val verticalScreenPadding: Dp = 16.dp
)

val LocalSpacing = staticCompositionLocalOf { Dimensions() }