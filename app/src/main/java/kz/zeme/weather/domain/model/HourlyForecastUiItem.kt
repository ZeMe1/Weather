package kz.zeme.weather.domain.model

import kz.zeme.weather.core.extensions.UiText

sealed interface HourlyForecastUiItem {
    data class Forecast(
        val label: UiText,
        val iconCode: String,
        val temp: Int
    ) : HourlyForecastUiItem

    data class SunEvent(
        val timeLabel: UiText,
        val isSunrise: Boolean
    ) : HourlyForecastUiItem
}