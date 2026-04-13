package kz.zeme.weather.shared.resources

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class WeatherShapes(
    val card: Shape,
    val surface: Shape,
    val bottomSheet: Shape
)

val defaultWeatherShapes = WeatherShapes(
    card = RoundedCornerShape(12.dp),
    surface = RoundedCornerShape(40.dp),
    bottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
)

val LocalWeatherShapes = staticCompositionLocalOf { defaultWeatherShapes }