package kz.zeme.weather.data.repository

import kz.zeme.weather.core.repository.BaseRepository
import kz.zeme.weather.core.repository.NetworkException
import kz.zeme.weather.core.repository.apiCall
import kz.zeme.weather.data.remote.api.GeocodingApi
import kz.zeme.weather.data.remote.mapper.GeocodingMapper
import kz.zeme.weather.domain.model.City
import kz.zeme.weather.domain.repository.GeocodingRepository
import kz.zeme.weather.domain.service.NetworkService

class GeocodingRepositoryImpl(
    private val api: GeocodingApi,
    private val mapper: GeocodingMapper,
    private val networkService: NetworkService,
): BaseRepository, GeocodingRepository {
    override suspend fun getCities(query: String): Result<List<City>> {
        if (!networkService.checkForConnectivity()) {
            return Result.failure(NetworkException.NoInternet)
        }
        return apiCall {
            api.getCities(query).map { mapper.map(it) }
        }
    }
}