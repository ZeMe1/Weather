package kz.zeme.weather.core.repository

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.SerializationException
import kz.zeme.weather.core.mapper.BaseMapper
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

interface BaseRepository {

    fun <R> handleSuccess(result: R): Result<R> = Result.success(result)

    fun <R> handleError(throwable: Throwable): Result<R> {
        return Result.failure(throwable.toDomainThrowable())
    }
}

suspend inline fun <T> BaseRepository.apiCall(crossinline request: suspend () -> T): Result<T> =
    runCatching {
        request()
    }.fold(::handleSuccess, ::handleError)

suspend fun <FROM, TO> BaseRepository.mappedApiCall(mapper: BaseMapper<FROM, TO>, call: suspend () -> FROM): Result<TO> =
    runCatching { call.invoke().let(mapper::map) }.fold(::handleSuccess, ::handleError)

suspend fun <FROM, TO> BaseRepository.mappedApiCallList(mapper: BaseMapper<FROM, TO>, call: suspend () -> List<FROM>): Result<List<TO>> =
    runCatching { call().map(mapper::map) }.fold(::handleSuccess, ::handleError)


private fun Throwable.toDomainThrowable(): Throwable = when (this) {
    is HttpException -> NetworkException.Http(this.code(), this.response()?.errorBody()?.string())
    is SocketTimeoutException,
    is TimeoutCancellationException -> NetworkException.RequestTimeout
    is IOException -> NetworkException.NoInternet
    is SerializationException -> NetworkException.Serialization(message)
    else -> NetworkException.Unknown(message)
}
