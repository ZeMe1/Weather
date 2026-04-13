package kz.zeme.weather.data.remote.api

import kz.zeme.weather.data.remote.dto.WeatherResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherResponseDto
}