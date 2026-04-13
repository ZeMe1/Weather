package kz.zeme.weather.data.repository

import kz.zeme.weather.data.mapper.WeatherMapper
import kz.zeme.weather.data.remote.api.WeatherApi
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val mapper: WeatherMapper
): WeatherRepository {
    override suspend fun getWeather(coordinates: Coordinates): Result<Weather> {
        return try {
            val response = api.getWeather(latitude = coordinates.latitude, longitude = coordinates.longitude)
            Result.success(mapper.map(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}