package kz.zeme.weather.domain.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.repository.WeatherRepository
import kz.zeme.weather.domain.service.LocationService

class GetWeatherUseCase(
    private val locationService: LocationService,
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<Result<Weather>> = flow {
        val coordinates = try {
            locationService.getCurrentCoordinates()
        } catch (e: LocationException) {
            emit(Result.failure(e))
            emitAll(
                repository.getCachedWeather()
                    .map { weather -> weather?.let { Result.success(it) } ?: Result.failure(e) }
                    .distinctUntilChanged()
            )
            return@flow
        }
        emitAll(repository.getWeather(coordinates))
    }
}