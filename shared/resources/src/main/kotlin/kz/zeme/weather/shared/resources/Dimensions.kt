package kz.zeme.weather.shared.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val borderWidth: Dp,
    val blurRadius: Dp,

    val extraSmallPadding: Dp,
    val smallPadding: Dp,
    val smallMediumPadding: Dp,
    val mediumPadding: Dp,
    val mediumLargePadding: Dp,
    val largePadding: Dp,
    val extraLargePadding: Dp,

    val horizontalScreenPadding: Dp,
    val verticalScreenPadding: Dp
)

val defaultWeatherDimensions = Dimensions(
    borderWidth = 1.dp,
    blurRadius = 4.dp,

    extraSmallPadding = 4.dp,
    smallPadding = 8.dp,
    smallMediumPadding = 12.dp,
    mediumPadding = 16.dp,
    mediumLargePadding = 20.dp,
    largePadding = 24.dp,
    extraLargePadding = 32.dp,

    horizontalScreenPadding = 16.dp,
    verticalScreenPadding = 16.dp
)

val LocalSpacing = staticCompositionLocalOf { defaultWeatherDimensions }