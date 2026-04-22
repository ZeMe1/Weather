package kz.zeme.weather.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import kz.zeme.weather.core.extensions.toUvDescription
import kz.zeme.weather.core.extensions.toUvLevelLabel
import kz.zeme.weather.core.extensions.toWindDirectionLabel
import kz.zeme.weather.core.utils.buildFeelsLikeDescription
import kz.zeme.weather.domain.model.DailyForecastUiItem
import kz.zeme.weather.domain.model.HourlyForecastUiItem
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.model.resolveWeatherBackground
import kz.zeme.weather.shared.resources.R
import kotlin.math.roundToInt

@Immutable
data class HomeState(
    val weatherData: Weather? = null,
    val historicalAverageTemp: Int? = null,
    val listOfHourlyForecastItems: List<HourlyForecastUiItem> = emptyList(),
    val listOfDailyForecastItems: List<DailyForecastUiItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
) {
    @get:DrawableRes
    val backgroundRes: Int
        get() = weatherData
            ?.let { resolveWeatherBackground(it.iconCode, it.timezone) }
            ?.drawableRes
            ?: R.drawable.ic_bg_after6

    val uvLevelLabel: Int
        get() = weatherData?.uvIndex?.roundToInt()?.toUvLevelLabel() ?: R.string.low

    val uvDescriptionLabel: Int
        get() = weatherData?.uvIndex?.roundToInt()?.toUvDescription() ?: R.string.uv_desc_low

    val feelsLikeDescription: Int
        get() = weatherData?.let {
            buildFeelsLikeDescription(it.currentTemp, it.feelsLikeTemp)
        } ?: R.string.feels_like_warmer

    val windDirectionLabel: Int
        get() = weatherData?.windDirectionDegrees?.toWindDirectionLabel() ?: R.string.wind_direction_ne


}

sealed interface HomeIntent {
    data object RefreshWeather : HomeIntent
}

sealed interface HomeAction {
    data object LoadScreen : HomeAction
}

sealed interface HomeMsg {
    data class WeatherDataLoaded(val weather: Weather) : HomeMsg
    data class HistoricalAverageTempLoaded(val temp: Int) : HomeMsg
    data class HourlyForecastItemsLoaded(val hourlyForecastItems: List<HourlyForecastUiItem>) : HomeMsg
    data class DailyForecastItemsLoaded(val dailyForecastItems: List<DailyForecastUiItem>) : HomeMsg
    data class Loading(val isLoading: Boolean) : HomeMsg
    data class Refreshing(val isRefreshing: Boolean) : HomeMsg
}

sealed interface HomeLabel {
    data class ShowError(val message: Int) : HomeLabel
}