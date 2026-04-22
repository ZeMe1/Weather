package kz.zeme.weather.data.remote.api

import kz.zeme.weather.data.remote.dto.TimeMachineResponseDto
import kz.zeme.weather.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherResponseDto

    @GET("data/3.0/onecall/timemachine")
    suspend fun getHistoryWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") timeStamp: Long
    ): TimeMachineResponseDto
}