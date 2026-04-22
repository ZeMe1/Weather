package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun HeaderBlock(
    weatherData: Weather
) {
    val maxText = "${stringResource(R.string.max)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}${weatherData.maxTempToday}${SymbolConstants.DEGREE}"
    val minText = "${stringResource(R.string.min)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}${weatherData.minTempToday}${SymbolConstants.DEGREE}"

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_cursor),
                contentDescription = null,
                tint = WeatherTheme.colors.white
            )
            Text(
                text = stringResource(R.string.hardcode_work), // TODO for now it is hardcoded, until I add some new logic for weather editing
                style = WeatherTheme.typography.weight500Size12LineHeight12,
                color = WeatherTheme.colors.textWhite
            )
        }
        Text(
            text = weatherData.cityName,
            style = WeatherTheme.typography.weight500Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )
        Text(
            text = "${weatherData.currentTemp}${SymbolConstants.DEGREE}",
            style = WeatherTheme.typography.weight100Size92LineHeight83,
            color = WeatherTheme.colors.textWhite
        )
        Spacer(Modifier.height(WeatherTheme.dimensions.extraSmallPadding))
        Text(
            text = weatherData.conditionText,
            style = WeatherTheme.typography.weight500Size18LineHeight21,
            color = WeatherTheme.colors.textCyan
        )
        Text(
            text = "$maxText${SymbolConstants.COMMA} $minText",
            style = WeatherTheme.typography.weight500Size18LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
    }
}