package kz.zeme.weather.core.repository

sealed class NetworkException(message: String? = null) : Exception(message) {
    data object NoInternet : NetworkException()
    data object RequestTimeout : NetworkException()
    data class Http(val code: Int, val body: String?) : NetworkException("HTTP $code")
    data class Serialization(val details: String?) : NetworkException(details)
    data class Unknown(val details: String?) : NetworkException(details)
}