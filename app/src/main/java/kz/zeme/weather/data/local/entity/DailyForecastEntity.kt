package kz.zeme.weather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_forecast",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["id"],
        childColumns = ["weather_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("weather_id")]
)
data class DailyForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "weather_id") val weatherId: Int = 0,
    val timeEpoch: Long,
    val minTemp: Int,
    val maxTemp: Int,
    val iconCode: String,
    val summary: String,
    val sunriseEpoch: Long,
    val sunsetEpoch: Long
)
