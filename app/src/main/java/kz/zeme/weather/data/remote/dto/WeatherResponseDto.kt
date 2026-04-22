package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("timezone") val timeZone: String,
    @SerializedName("current") val currentWeather: CurrentWeatherDto,
    val hourly: List<HourlyDto>,
    val daily: List<DailyDto>
)