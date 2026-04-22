package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DailyDto(
    @SerializedName("dt") val dateTime: Long,
    val summary: String,
    val temp: DailyTempDto,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    val weather: List<WeatherConditionDto>,
    @SerializedName("wind_speed") val windSpeed: Double
)
