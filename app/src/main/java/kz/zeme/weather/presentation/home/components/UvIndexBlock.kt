package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun UvIndexBlock(
    uvIndex: Int,
    levelLabel: String,
    description: String,
    modifier: Modifier = Modifier
) {
    CommonWeatherDetailCard(
        headerText = stringResource(R.string.uv_index),
        headerIcon = R.drawable.ic_sun,
        modifier = modifier
    ) {
        Text(
            text = uvIndex.toString(),
            style = WeatherTheme.typography.weight500Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )
        Text(
            text = levelLabel,
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
        Spacer(Modifier.height(WeatherTheme.dimensions.smallPadding))
        UvGradientBar(uvIndex = uvIndex.toDouble())
        Spacer(Modifier.height(WeatherTheme.dimensions.smallMediumPadding).weight(1f))
        Text(
            text = description,
            style = WeatherTheme.typography.weight400Size15LineHeight18,
            color = WeatherTheme.colors.textWhite
        )
    }
}

@Composable
private fun UvGradientBar(uvIndex: Double) {
    val maxUv = 11f
    val fraction = (uvIndex / maxUv).toFloat().coerceIn(0f, 1f)

    val gradientColors = listOf( // TODO add to WeatherTheme colors
        Color(0xFF_68D548),
        Color(0xFF_ECD70C),
        Color(0xFF_FAA02E),
        Color(0xFF_FB3D57),
        Color(0xFF_D73BDE)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(WeatherTheme.shapes.chip)
    ) {
        // Full spectrum track
        drawRect(
            brush = Brush.horizontalGradient(colors = gradientColors),
            size = size
        )
        // Dimmed overlay to the right of current UV position
        drawRect(
            color = Color.Black.copy(alpha = 0.45f),
            topLeft = Offset(size.width * fraction, 0f),
            size = Size(size.width * (1f - fraction), size.height)
        )
    }
}