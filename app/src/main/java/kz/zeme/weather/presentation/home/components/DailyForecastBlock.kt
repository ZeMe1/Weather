package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonHorizontalDivider
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.core.commonComponents.commonWidgets.DailyForecastRow
import kz.zeme.weather.domain.model.DailyForecastUiItem
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun DailyForecastBlock(
    listOfDailyForecast: List<DailyForecastUiItem>
) {
    val globalMin = listOfDailyForecast.minOfOrNull { it.minTemp } ?: 0
    val globalMax = listOfDailyForecast.maxOfOrNull { it.maxTemp } ?: 0

    CommonWeatherDetailCard(
        headerText = stringResource(R.string.forecast_for_eight_days),
        headerIcon = R.drawable.ic_calendar
    ) {
        listOfDailyForecast.forEach { dailyForecast ->
            CommonHorizontalDivider()
            DailyForecastRow(
                dailyForecast = dailyForecast,
                globalMin = globalMin,
                globalMax = globalMax,
                modifier = Modifier.padding(vertical = WeatherTheme.dimensions.smallPadding)
            )
        }
    }
}