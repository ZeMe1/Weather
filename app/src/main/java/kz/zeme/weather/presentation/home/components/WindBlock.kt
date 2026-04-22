package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonHorizontalDivider
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.core.commonComponents.commonWidgets.WindInformationRow
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun WindBlock(
    windSpeed: String,
    windGustSpeed: String,
    windDirectionDegree: String,
    windDirectionLabel: String
) {
    CommonWeatherDetailCard(
        headerText = stringResource(R.string.wind),
        headerIcon = R.drawable.ic_wind_speed
    ) {
        WindInformationRow(
            leftText = stringResource(R.string.wind),
            rightText = "$windSpeed ${stringResource(R.string.km_per_hour)}",
            modifier = Modifier.padding(vertical = WeatherTheme.dimensions.smallPadding)
        )
        CommonHorizontalDivider()
        WindInformationRow(
            leftText = stringResource(R.string.wind_gusts),
            rightText = "$windGustSpeed ${stringResource(R.string.km_per_hour)}",
            modifier = Modifier.padding(vertical = WeatherTheme.dimensions.smallPadding)
        )
        CommonHorizontalDivider()
        WindInformationRow(
            leftText = stringResource(R.string.direction),
            rightText = "$windDirectionDegree${SymbolConstants.DEGREE} $windDirectionLabel",
            modifier = Modifier.padding(vertical = WeatherTheme.dimensions.smallPadding)
        )
        CommonHorizontalDivider()
    }
}