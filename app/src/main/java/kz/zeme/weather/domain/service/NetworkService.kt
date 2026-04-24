package kz.zeme.weather.domain.service

interface NetworkService {
    fun checkForConnectivity(): Boolean
}