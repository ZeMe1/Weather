package kz.zeme.weather.data.remote.dto

data class WeatherConditionDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)