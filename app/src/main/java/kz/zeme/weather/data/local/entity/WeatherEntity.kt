package kz.zeme.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: String,
    val unit: String,
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
    val sunriseEpoch: Long,
    val sunsetEpoch: Long
) {
    companion object {
        fun buildId(lat: Double, lon: Double): String {
            val rLat = "%.2f".format(lat)
            val rLon = "%.2f".format(lon)
            return "${rLat}_${rLon}"
        }
    }
}
