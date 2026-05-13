package kz.zeme.weather.domain.useCase

import kz.zeme.weather.domain.model.City
import kz.zeme.weather.domain.repository.GeocodingRepository

class GetGeocodingUseCase(
    private val repository: GeocodingRepository
) {
    suspend operator fun invoke(query: String): Result<List<City>> {
        return repository.getCities(query)
    }
}