package kz.zeme.weather.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kz.zeme.weather.core.repository.BaseRepository
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.core.repository.NetworkException
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
import kz.zeme.weather.domain.service.NetworkService

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val locationService: LocationService,
    private val networkService: NetworkService,
    private val weatherMapper: WeatherMapper,
    private val hourlyMapper: HourlyForecastMapper,
    private val dailyMapper: DailyForecastMapper,
    private val historyWeatherMapper: HistoryWeatherMapper,
    private val weatherMapperLocal: WeatherMapperLocal,
) : BaseRepository, WeatherRepository {

    override fun getWeather(coordinates: Coordinates): Flow<Result<Weather>> = channelFlow {
        launch {
            dao.getWeatherForecast()
                .filterNotNull()
                .map { Result.success(weatherMapperLocal.map(it)) }
                .collect { send(it) }
        }

        if (!networkService.checkForConnectivity()) {
            send(Result.failure(NetworkException.NoInternet))
            return@channelFlow
        } else {
            val refreshResult = refreshNetwork(coordinates)

            refreshResult.onFailure { exception ->
                send(Result.failure(exception))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun getCachedWeather(): Flow<Weather?> {
        return dao.getWeatherForecast().map { weatherDataEntity ->
            weatherDataEntity?.let { weatherMapperLocal.map(it) }
        }.flowOn(Dispatchers.IO)
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

    private suspend fun refreshNetwork(coordinates: Coordinates): Result<Unit> {
        return try {
            coroutineScope {
                val cityNameDeferred = async {
                    try {
                        locationService.getCityName(coordinates.latitude, coordinates.longitude)
                    } catch (e: LocationException.GpsDisabled) {
                        throw e
                    } catch (e: Exception) {
                        null
                    }
                }

                val responseDeferred = async {
                    apiCall { api.getWeather(coordinates.latitude, coordinates.longitude) }
                }

                val cityName = cityNameDeferred.await()
                val responseResult = responseDeferred.await()

                val response = responseResult.getOrThrow()

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

                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val HOURS_IN_DAY = 24
    }
}