package kz.zeme.weather.data.remote.dto

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)