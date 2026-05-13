package kz.zeme.weather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hourly_forecast",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["id"],
        childColumns = ["weather_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("weather_id")]
)
data class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "weather_id") val weatherId: String = "",
    val timeEpoch: Long,
    val temp: Int,
    val iconCode: String
)
