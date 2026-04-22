package kz.zeme.weather.domain.model

import kotlinx.datetime.Instant

data class Weather(
    val cityName: String,
    val timezone: String,
    val currentTemp: Int,
    val feelsLikeTemp: Int,
    val conditionText: String,
    val iconCode: String,
    val minTempToday: Int,
    val maxTempToday: Int,
    val windSpeed: Double,
    val windGust: Double?,
    val windDirectionDegrees: Int,
    val humidity: Int,
    val pressure: Int,
    val uvIndex: Double,
    val dewPoint: Int,
    val sunrise: Instant,
    val sunset: Instant,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)