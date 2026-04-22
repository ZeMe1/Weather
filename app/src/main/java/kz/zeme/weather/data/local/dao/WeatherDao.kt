package kz.zeme.weather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kz.zeme.weather.data.local.entity.DailyForecastEntity
import kz.zeme.weather.data.local.entity.HourlyForecastEntity
import kz.zeme.weather.data.local.entity.WeatherEntity
import kz.zeme.weather.data.local.entity.WeatherForecastData

@Dao
interface WeatherDao {

    @Transaction
    @Query("SELECT * FROM weather WHERE id = 0")
    fun getWeatherForecast(): Flow<WeatherForecastData?>

    @Transaction
    suspend fun upsertWeather(weather: WeatherEntity, hourly: List<HourlyForecastEntity>, daily: List<DailyForecastEntity>) {
        insertWeather(weather)
        deleteHourlyForecast(weather.id)
        deleteDailyForecast(weather.id)
        insertHourly(hourly)
        insertDaily(daily)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourly(hourly: List<HourlyForecastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaily(daily: List<DailyForecastEntity>)

    @Query("DELETE FROM hourly_forecast WHERE weather_id = :weatherId")
    suspend fun deleteHourlyForecast(weatherId: Int)

    @Query("DELETE FROM daily_forecast WHERE weather_id = :weatherId")
    suspend fun deleteDailyForecast(weatherId: Int)
}