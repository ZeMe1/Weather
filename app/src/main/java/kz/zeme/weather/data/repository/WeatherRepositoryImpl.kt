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
import kz.zeme.weather.data.local.entity.WeatherEntity
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
        val locationId = WeatherEntity.buildId(coordinates.latitude, coordinates.longitude)

        launch {
            dao.getWeatherForecast(locationId)
                .filterNotNull()
                .map { Result.success(weatherMapperLocal.map(it)) }
                .collect { send(it) }
        }

        if (!networkService.checkForConnectivity()) {
            send(Result.failure(NetworkException.NoInternet))
            return@channelFlow
        } else {
            refreshNetwork(coordinates, locationId).onFailure { exception ->
                send(Result.failure(exception))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun getCachedWeather(locationId: String): Flow<Weather?> {
        return dao.getWeatherForecast(locationId)
            .map { it?.let { weatherMapperLocal.map(it) } }
            .flowOn(Dispatchers.IO)
    }

    override fun getAllCachedWeather(): Flow<List<Weather>> {
        return dao.getAllWeatherForecasts()
            .map { list -> list.map { weatherMapperLocal.map(it) } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun deleteLocation(coordinates: Coordinates) {
        val locationId = WeatherEntity.buildId(coordinates.latitude, coordinates.longitude)
        dao.deleteLocation(locationId)
    }

    override suspend fun getHistoryWeather(
        coordinates: Coordinates,
        timeStamp: Long
    ): Result<HistoryWeather> = apiCall {
        val response = api.getHistoryWeather(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            timeStamp = timeStamp
        )
        historyWeatherMapper.map(response.data.first())
    }

    private suspend fun refreshNetwork(
        coordinates: Coordinates,
        locationId: String
    ): Result<Unit> {
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
                val response = responseDeferred.await().getOrThrow()

                val source = WeatherSource(
                    weatherResponse = response,
                    hourlyForecast = response.hourly.take(HOURS_IN_DAY).map { hourlyMapper.map(it) },
                    dailyForecast = response.daily.map { dailyMapper.map(it) }
                )

                val weatherData = weatherMapper.map(source).copy(
                    id = locationId,
                    cityName = cityName ?: response.timeZone
                        .substringAfterLast("/")
                        .replace("_", " ")
                )

                weatherMapperLocal.reverse(weatherData).let {
                    dao.upsertWeather(
                        weather = it.weather,
                        hourly = it.hourlyForecasts,
                        daily = it.dailyForecasts
                    )
                }

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