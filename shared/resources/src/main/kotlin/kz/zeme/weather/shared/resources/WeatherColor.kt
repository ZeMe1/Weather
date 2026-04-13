package kz.zeme.weather.shared.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LightColors = WeatherColor(
    textWhite = Color(0xFF_FFFBFC),
    cardCyan = Color(0xFF_377DC3),
    backgroundBlue = Color(0xFF_2B4F73),
    textCyan = Color(0xFF_C9FFFA),
    tiffanyBlue = Color(0xFF_60DACF),
    bottomSheetBackground = Color(0xFF_424D58),
    textFieldBackground = Color(0xFF_0C2741),
    white = Color.White,
)

internal val DarkColors = WeatherColor(
    textWhite = Color(0xFF_FFFBFC),
    cardCyan = Color(0xFF_377DC3),
    backgroundBlue = Color(0xFF_2B4F73),
    textCyan = Color(0xFF_C9FFFA),
    tiffanyBlue = Color(0xFF_60DACF),
    bottomSheetBackground = Color(0xFF_424D58),
    textFieldBackground = Color(0xFF_0C2741),
    white = Color.White,
)


@Immutable
class WeatherColor(
    textWhite: Color,
    textCyan: Color,
    cardCyan: Color,
    backgroundBlue: Color,
    tiffanyBlue: Color,
    bottomSheetBackground: Color,
    textFieldBackground: Color,
    white: Color,
) {
    var textWhite by mutableStateOf(textWhite)
        private set

    var white by mutableStateOf(white)
        private set

    var cardCyan by mutableStateOf(cardCyan)
        private set

    var backgroundBlue by mutableStateOf(backgroundBlue)
        private set

    var textCyan by mutableStateOf(textCyan)
        private set

    var tiffanyBlue by mutableStateOf(tiffanyBlue)
        private set

    var bottomSheetBackground by mutableStateOf(bottomSheetBackground)
        private set

    var textFieldBackground by mutableStateOf(textFieldBackground)
        private set

    fun copy(
        textWhite: Color = this.textWhite,
        white: Color = this.white,
        cardCyan: Color = this.cardCyan,
        backgroundBlue: Color = this.backgroundBlue,
        textCyan: Color = this.textCyan,
        tiffanyBlue: Color = this.tiffanyBlue,
        bottomSheetBackground: Color = this.bottomSheetBackground,
        textFieldBackground: Color = this.textFieldBackground,
    ) = WeatherColor(
        textWhite = textWhite,
        white = white,
        cardCyan = cardCyan,
        backgroundBlue = backgroundBlue,
        textCyan = textCyan,
        tiffanyBlue = tiffanyBlue,
        bottomSheetBackground = bottomSheetBackground,
        textFieldBackground = textFieldBackground,
    )

    fun updateColorsFrom(other: WeatherColor) {
        textWhite = other.textWhite
        white = other.white
        cardCyan = other.cardCyan
        backgroundBlue = other.backgroundBlue
        textCyan = other.textCyan
        tiffanyBlue = other.tiffanyBlue
        bottomSheetBackground = other.bottomSheetBackground
        textFieldBackground = other.textFieldBackground
    }
}

val LocalColors = staticCompositionLocalOf { LightColors }