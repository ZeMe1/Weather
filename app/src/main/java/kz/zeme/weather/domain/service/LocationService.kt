package kz.zeme.weather.domain.service

import kz.zeme.weather.domain.model.Coordinates

interface LocationService {
    suspend fun getCurrentCoordinates(): Coordinates
    suspend fun getCityName(latitude: Double, longitude: Double): String?
}