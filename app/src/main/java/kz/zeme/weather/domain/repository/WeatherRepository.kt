package kz.zeme.weather.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.model.HistoryWeather
import kz.zeme.weather.domain.model.Weather

interface WeatherRepository {
    fun getWeather(coordinates: Coordinates): Flow<Result<Weather>>
    fun getCachedWeather(locationId: String): Flow<Weather?>
    fun getAllCachedWeather(): Flow<List<Weather>>
    suspend fun deleteLocation(coordinates: Coordinates)
    suspend fun getHistoryWeather(coordinates: Coordinates, timeStamp: Long): Result<HistoryWeather>
}