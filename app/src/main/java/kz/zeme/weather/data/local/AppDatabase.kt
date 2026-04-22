package kz.zeme.weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.zeme.weather.data.local.converter.InstantConverter
import kz.zeme.weather.data.local.dao.WeatherDao
import kz.zeme.weather.data.local.entity.DailyForecastEntity
import kz.zeme.weather.data.local.entity.HourlyForecastEntity
import kz.zeme.weather.data.local.entity.WeatherEntity


@Database(
    entities = [WeatherEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDao
}