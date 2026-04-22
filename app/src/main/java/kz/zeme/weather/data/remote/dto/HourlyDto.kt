package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HourlyDto(
    @SerializedName("dt") val dateTime: Long,
    val temp: Double,
    val weather: List<WeatherConditionDto>
)
