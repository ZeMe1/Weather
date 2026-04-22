package kz.zeme.weather.domain.model

import kotlinx.datetime.Instant

data class HourlyForecast(
    val time: Instant,
    val temp: Int,
    val iconCode: String
)
