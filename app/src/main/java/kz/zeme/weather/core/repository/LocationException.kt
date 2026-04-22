package kz.zeme.weather.core.repository

sealed class LocationException : Exception() {
    data object GpsDisabled : LocationException()
    data object PermissionDenied : LocationException()
    data object LocationUnavailable : LocationException()
}