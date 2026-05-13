package kz.zeme.weather.domain.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.data.local.entity.WeatherEntity
import kz.zeme.weather.domain.model.Coordinates
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
        } catch (e: LocationException.GpsDisabled) {
            repository.getAllCachedWeather()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { emit(Result.success(it)) }

            emit(Result.failure(e))
            return@flow
        } catch (e: LocationException) {
            emit(Result.failure(e))
            return@flow
        }

        val locationId = WeatherEntity.buildId(coordinates.latitude, coordinates.longitude)

        val initialCache = repository.getCachedWeather(locationId).firstOrNull()
        if (initialCache != null) {
            emit(Result.success(initialCache))
        }

        emitAll(repository.getWeather(coordinates))
    }.flowOn(Dispatchers.IO)

    fun invoke(latitude: Double, longitude: Double): Flow<Result<Weather>> {
        val locationId = WeatherEntity.buildId(latitude, longitude)
        return flow {
            val initialCache = repository.getCachedWeather(locationId).firstOrNull()
            if (initialCache != null) {
                emit(Result.success(initialCache))
            }
            emitAll(repository.getWeather(Coordinates(latitude, longitude)))
        }.flowOn(Dispatchers.IO)
    }
}