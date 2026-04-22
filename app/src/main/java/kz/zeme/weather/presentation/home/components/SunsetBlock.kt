package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun SunsetBlock(
    sunsetTime: String,
    sunriseTime: String,
    modifier: Modifier = Modifier
) {
    CommonWeatherDetailCard(
        headerText = stringResource(R.string.sunset),
        headerIcon = R.drawable.ic_sunset,
        modifier = modifier
    ) {
        Text(
            text = sunsetTime,
            style = WeatherTheme.typography.weight500Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "${stringResource(R.string.sunrise_at)} $sunriseTime${SymbolConstants.DOT}",
            style = WeatherTheme.typography.weight400Size15LineHeight18,
            color = WeatherTheme.colors.textWhite
        )
    }
}