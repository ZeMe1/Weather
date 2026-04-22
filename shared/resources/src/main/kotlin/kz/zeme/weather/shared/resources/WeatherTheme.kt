package kz.zeme.weather.shared.resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun WeatherTheme(
    content: @Composable () -> Unit
) {
    val typography = defaultWeatherTypography()
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    val shapes = defaultWeatherShapes
    val dimensions = defaultWeatherDimensions

    WeatherThemeImpl(
        typography = typography,
        colors = colors,
        shapes = shapes,
        dimensions = dimensions,
        content = content
    )
}

@Composable
fun WeatherThemeImpl(
    typography: WeatherTypography,
    colors: WeatherColor,
    dimensions: Dimensions,
    shapes: WeatherShapes,
    content: @Composable () -> Unit
) {
    val colors = remember {
        colors.copy()
    }.apply { updateColorsFrom(colors) }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalWeatherTypography provides typography,
        LocalWeatherShapes provides shapes,
        LocalSpacing provides dimensions
    ) {
        ProvideTextStyle(value = typography.weight500Size15LineHeight21, content = content)
    }
}

object WeatherTheme {
    val colors: WeatherColor
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: WeatherTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherTypography.current

    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val shapes: WeatherShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherShapes.current
}