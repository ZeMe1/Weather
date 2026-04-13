package kz.zeme.weather.domain.repository

import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.model.Weather

interface WeatherRepository {
    suspend fun getWeather(coordinates: Coordinates): Result<Weather>
}