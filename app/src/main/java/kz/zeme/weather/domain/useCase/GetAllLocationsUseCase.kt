package kz.zeme.weather.domain.useCase

import kotlinx.coroutines.flow.Flow
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.repository.WeatherRepository

class GetAllLocationsUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<Weather>> {
        return repository.getAllCachedWeather()
    }
}