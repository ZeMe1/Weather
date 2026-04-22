package kz.zeme.weather.core.architecture

import kz.zeme.weather.core.repository.NetworkException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

interface ErrorHandler {
    fun handleError(throwable: Throwable)
    fun createErrorMessage(throwable: Throwable): String
    fun getErrorCode(throwable: Throwable): Int = throwable.retrofitErrorCode()
}

class DefaultErrorHandler : ErrorHandler {
    private val tag = "MVI"

    override fun handleError(throwable: Throwable) {
        platformLogError(tag, "Error in MVI flow", throwable)
    }

    override fun createErrorMessage(throwable: Throwable): String {
        return handleApiError(throwable).message
    }
}

data class ApiError(val code: Int = -1, val message: String)

fun handleApiError(t: Throwable): ApiError {
    val code = t.retrofitErrorCode()

    val message = when (t) {
        is HttpException -> {
            when (t.code()) {
                in 300..399 -> "Redirect (${t.code()})"
                in 400..499 -> "Request error (${t.code()})"
                in 500..599 -> "Server error (${t.code()})"
                else -> "HTTP error (${t.code()})"
            }
        }
        is SocketTimeoutException -> "Request timeout"
        is IOException -> "Network error / No internet connection"

        is NetworkException.Http -> "HTTP error (${t.code})"
        is NetworkException.NoInternet -> "No internet connection"
        is NetworkException.Serialization -> "Data parsing error"
        is NetworkException.Unknown -> t.message ?: "Unknown error"

        else -> t.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
    }
    return ApiError(code = code, message = message)
}

/** Retrofit & Domain-friendly status extraction */
fun Throwable.retrofitErrorCode(): Int = when (this) {
    is HttpException -> this.code()
    is NetworkException.Http -> this.code
    else -> -1
}

/** Platform logging */
fun platformLogError(tag: String, message: String, throwable: Throwable) {

}