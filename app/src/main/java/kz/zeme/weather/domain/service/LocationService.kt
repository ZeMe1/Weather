package kz.zeme.weather.domain.service

import kz.zeme.weather.domain.model.Coordinates

interface LocationService {
    suspend fun getCurrentCoordinates(): Coordinates?
}