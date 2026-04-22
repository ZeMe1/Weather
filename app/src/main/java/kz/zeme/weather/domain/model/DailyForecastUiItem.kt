package kz.zeme.weather.domain.model

import kz.zeme.weather.core.extensions.UiText

data class DailyForecastUiItem(
    val dayLabel: UiText,
    val iconCode: String,
    val minTemp: Int,
    val maxTemp: Int,
    val summary: String
)