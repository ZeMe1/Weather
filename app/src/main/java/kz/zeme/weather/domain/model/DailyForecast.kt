package kz.zeme.weather.domain.model

import kotlinx.datetime.Instant

data class DailyForecast(
    val time: Instant,
    val minTemp: Int,
    val maxTemp: Int,
    val iconCode: String,
    val summary: String,
    val sunrise: Instant,
    val sunset: Instant
)
