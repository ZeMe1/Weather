package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.zeme.weather.core.extensions.asString
import kz.zeme.weather.core.extensions.fromIconCode
import kz.zeme.weather.domain.model.DailyForecastUiItem
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun DailyForecastRow(
    dailyForecast: DailyForecastUiItem,
    globalMin: Int,
    globalMax: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(96.dp),
            text = dailyForecast.dayLabel.asString(),
            style = WeatherTheme.typography.weight500Size18LineHeight21,
            color = WeatherTheme.colors.textWhite,
            maxLines = 1
        )

        Icon(
            painter = painterResource(dailyForecast.iconCode.fromIconCode()),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(28.dp)
        )

        Spacer(Modifier.width(WeatherTheme.dimensions.mediumPadding))

        TemperatureRangeBar(
            modifier = Modifier.weight(1f),
            globalMin = globalMin,
            globalMax = globalMax,
            dayMin = dailyForecast.minTemp,
            dayMax = dailyForecast.maxTemp
        )
    }
}

@Composable
private fun TemperatureRangeBar(
    globalMin: Int,
    globalMax: Int,
    dayMin: Int,
    dayMax: Int,
    modifier: Modifier = Modifier
) {
    val totalRange = (globalMax - globalMin).toFloat().coerceAtLeast(1f)
    val startFraction = ((dayMin - globalMin) / totalRange).coerceIn(0f, 1f)
    val endFraction = ((dayMax - globalMin) / totalRange).coerceIn(0f, 1f)

    val trackColor = Color.Black.copy(alpha = 0.1f)
    val itemColor = WeatherTheme.colors.tiffanyBlue

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
    ) {
        Text(
            text = "${dayMin}${SymbolConstants.DEGREE}",
            style = WeatherTheme.typography.weight500Size18LineHeight21,
            color = WeatherTheme.colors.textWhite.copy(alpha = 0.5f)
        )

        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
        ) {
            val trackHeight = size.height
            val cornerRadius = CornerRadius(trackHeight / 2)

            drawRoundRect(
                color = trackColor,
                size = size,
                cornerRadius = cornerRadius
            )

            val startX = size.width * startFraction
            val endX = size.width * endFraction
            val segmentWidth = (endX - startX).coerceAtLeast(trackHeight)

            drawRoundRect(
                color = itemColor,
                topLeft = Offset(startX, 0f),
                size = Size(segmentWidth, trackHeight),
                cornerRadius = cornerRadius
            )
        }

        Text(
            text = "${dayMax}${SymbolConstants.DEGREE}",
            style = WeatherTheme.typography.weight500Size18LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
    }
}