package kz.zeme.weather.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherForecastData(
    @Embedded val weather: WeatherEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "weather_id"
    )
    val hourlyForecasts: List<HourlyForecastEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "weather_id"
    )
    val dailyForecasts: List<DailyForecastEntity>
)