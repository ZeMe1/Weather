package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun BoxWithIcon(
    icon: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.Gray.copy(0f),
            Color.Gray.copy(0.3f)
        )
    ),
    borderColor: Color = WeatherTheme.colors.white.copy(0.5f),
    shape: Shape = WeatherTheme.shapes.surface,
    borderWidth: Dp = WeatherTheme.dimensions.borderWidth,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = borderWidth,
                shape = shape,
                color = borderColor
            )
            .background(backgroundColor)
            .safeClickable { onClick() }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = WeatherTheme.colors.white,
            modifier = Modifier
                .padding(WeatherTheme.dimensions.smallPadding)
                .size(WeatherTheme.dimensions.extraLargePadding)
        )
    }
}