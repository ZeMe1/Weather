package kz.zeme.weather.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kz.zeme.weather.core.repository.BaseRepository
import kz.zeme.weather.core.repository.apiCall
import kz.zeme.weather.data.local.dao.WeatherDao
import kz.zeme.weather.data.local.mapper.WeatherMapperLocal
import kz.zeme.weather.data.remote.api.WeatherApi
import kz.zeme.weather.data.remote.mapper.DailyForecastMapper
import kz.zeme.weather.data.remote.mapper.HistoryWeatherMapper
import kz.zeme.weather.data.remote.mapper.HourlyForecastMapper
import kz.zeme.weather.data.remote.mapper.WeatherMapper
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.model.HistoryWeather
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.model.WeatherSource
import kz.zeme.weather.domain.repository.WeatherRepository
import kz.zeme.weather.domain.service.LocationService

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val locationService: LocationService,
    private val weatherMapper: WeatherMapper,
    private val hourlyMapper: HourlyForecastMapper,
    private val dailyMapper: DailyForecastMapper,
    private val historyWeatherMapper: HistoryWeatherMapper,
    private val weatherMapperLocal: WeatherMapperLocal,
): BaseRepository, WeatherRepository {
    override fun getWeather(coordinates: Coordinates): Flow<Result<Weather>> = flow {

        val cachedData = dao.getWeatherForecast().firstOrNull()
        if (cachedData != null) {
            emit(Result.success(weatherMapperLocal.map(cachedData)))
        }

        val refreshResult = runCatching { refreshNetwork(coordinates) }
        if (refreshResult.isFailure) {
            if (cachedData == null) {
                emit(Result.failure(refreshResult.exceptionOrNull()!!))
                return@flow
            } else {
                emit(Result.failure(refreshResult.exceptionOrNull()!!))
            }
        }
        emitAll(
            dao.getWeatherForecast()
                .filterNotNull()
                .map { Result.success(weatherMapperLocal.map(it)) }
        )
    }

    override fun getCachedWeather(): Flow<Weather?> {
        return dao.getWeatherForecast().map { weatherDataEntity ->
            weatherDataEntity?.let { weatherMapperLocal.map(it) }
        }
    }

    override suspend fun getHistoryWeather(coordinates: Coordinates, timeStamp: Long): Result<HistoryWeather> =
        apiCall {
            val response = api.getHistoryWeather(
                latitude = coordinates.latitude,
                longitude = coordinates.longitude,
                timeStamp = timeStamp
            )
            val data = response.data.first()
            historyWeatherMapper.map(data)
        }

    private suspend fun refreshNetwork(coordinates: Coordinates) {
        apiCall {
            coroutineScope {
                val responseDeferred = async { api.getWeather(coordinates.latitude, coordinates.longitude) }
                val cityNameDeferred = async { locationService.getCityName(coordinates.latitude, coordinates.longitude) }

                val response = responseDeferred.await()
                val cityName = cityNameDeferred.await()

                val mappedHourly = response.hourly.take(HOURS_IN_DAY).map { hourlyMapper.map(it) }
                val mappedDaily = response.daily.map { dailyMapper.map(it) }

                val source = WeatherSource(
                    weatherResponse = response,
                    hourlyForecast = mappedHourly,
                    dailyForecast = mappedDaily
                )

                val weatherData = weatherMapper.map(source).copy(
                    cityName = cityName ?: response.timeZone.substringAfterLast("/")
                        .replace("_", " ")
                )
                val finalWeatherData = weatherMapperLocal.reverse(weatherData)

                dao.upsertWeather(
                    weather = finalWeatherData.weather,
                    hourly = finalWeatherData.hourlyForecasts,
                    daily = finalWeatherData.dailyForecasts
                )
            }
        }
    }

    companion object {
        private const val HOURS_IN_DAY = 24
    }
}