package kz.zeme.weather.data.remote.api

import kz.zeme.weather.data.remote.dto.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

    interface GeocodingApi {
        @GET("geo/1.0/direct")
        suspend fun getCities(
            @Query("q") query: String,
            @Query("limit") limit: Int = 10
        ): List<GeocodingResponseDto>
    }