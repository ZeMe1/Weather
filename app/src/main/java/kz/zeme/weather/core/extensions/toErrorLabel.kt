package kz.zeme.weather.core.extensions

import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.core.repository.NetworkException
import kz.zeme.weather.shared.resources.R

fun Throwable.toErrorLabel(): Int = when(this) {
    is NetworkException.RequestTimeout -> R.string.request_timeout
    is NetworkException.NoInternet -> R.string.no_internet
    is LocationException.GpsDisabled -> R.string.gps_disabled
    is LocationException.PermissionDenied -> R.string.permission_denied
    is LocationException.LocationUnavailable -> R.string.location_unavailable
    else -> R.string.something_went_wrong
}