package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonHorizontalDivider
import kz.zeme.weather.core.commonComponents.commonWidgets.HourlyForecastColumn
import kz.zeme.weather.core.extensions.asString
import kz.zeme.weather.core.extensions.fromIconCode
import kz.zeme.weather.domain.model.HourlyForecastUiItem
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun HourlyForecastBlock(
    descText: String,
    listOfHourlyForecast: List<HourlyForecastUiItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = WeatherTheme.colors.cardCyan.copy(0.5f)),
        shape = WeatherTheme.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding, vertical = WeatherTheme.dimensions.smallMediumPadding),
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallMediumPadding)
        ) {
            Text(
                text = descText,
                style = WeatherTheme.typography.weight400Size14LineHeight20,
                color = WeatherTheme.colors.textWhite
            )
            CommonHorizontalDivider()

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.largePadding)
            ) {
                items(listOfHourlyForecast) { hourlyForecast ->
                    when(hourlyForecast) {
                        is HourlyForecastUiItem.Forecast -> HourlyForecastColumn(
                            time = hourlyForecast.label.asString(),
                            icon = hourlyForecast.iconCode.fromIconCode(),
                            temp = hourlyForecast.temp.toString()
                        )
                        is HourlyForecastUiItem.SunEvent -> HourlyForecastColumn(
                            time = hourlyForecast.timeLabel.asString(),
                            icon = if (hourlyForecast.isSunrise) R.drawable.ic_sun else R.drawable.ic_sunset,
                            temp = if (hourlyForecast.isSunrise) stringResource(R.string.sunrise) else stringResource(R.string.sunset)
                        )
                    }
                }
            }
        }
    }
}