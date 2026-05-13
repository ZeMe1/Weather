package kz.zeme.weather.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherSummary(
    val id: String,
    val lat: Double,
    val lon: Double,
    val cityName: String,
    val weatherDescription: String,
    val lastFetchedTime: String,
    val weatherTemp: Int,
    val minWeatherTemp: Int,
    val maxWeatherTemp: Int,
    val backgroundRes: Int
)