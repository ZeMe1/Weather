package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.isDigitsOnly
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun HourlyForecastColumn(
    time: String,
    icon: Int,
    temp: String
) {
    val tempText = if (temp.isDigitsOnly()) "$temp${SymbolConstants.DEGREE}" else temp

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallMediumPadding)
    ) {
        Text(
            text = time,
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = tempText,
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
    }
}