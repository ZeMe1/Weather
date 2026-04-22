package kz.zeme.weather.domain.model

import kotlinx.datetime.Instant

data class HistoryWeather(
    val timeStamp: Instant,
    val temp: Int
)
