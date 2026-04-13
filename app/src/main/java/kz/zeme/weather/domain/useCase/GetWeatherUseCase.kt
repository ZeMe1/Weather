package kz.zeme.weather.domain.useCase

import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.repository.WeatherRepository
import kz.zeme.weather.domain.service.LocationService

class GetWeatherUseCase(
    private val locationService: LocationService,
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Result<Weather> {
        val location = locationService.getCurrentCoordinates()
        return if (location != null) {
            repository.getWeather(location)
        } else {
            Result.failure(Exception("BRO, location is null")) // TODO Just for fun, will change it later
        }
    }
}