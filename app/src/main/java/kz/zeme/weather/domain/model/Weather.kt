package kz.zeme.weather.domain.model

data class Weather(
    val cityName: String,
    val currentTemp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val feelsLikeTemp: Int,
    val condition: String, // clear or cloud for example
    val windSpeed: Double,
    val windDirectionDegrees: Int,
    val humidity: Int,
    val pressure: Int,
    val iconCode: String,
    val sunrise: Long,
    val sunset: Long
)