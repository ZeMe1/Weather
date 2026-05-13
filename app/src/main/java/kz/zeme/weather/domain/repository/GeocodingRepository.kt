package kz.zeme.weather.domain.repository

import kz.zeme.weather.domain.model.City

interface GeocodingRepository {
    suspend fun getCities(query: String): Result<List<City>>
}