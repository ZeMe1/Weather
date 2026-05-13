package kz.zeme.weather.data.remote.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.extensions.capitalizeFirst
import kz.zeme.weather.core.mapper.BaseMapper
import kz.zeme.weather.core.preference.TemperatureUnitPreferences
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.model.WeatherSource
import kotlin.math.roundToInt

class WeatherMapper(
    private val temperaturePrefs: TemperatureUnitPreferences,
): BaseMapper<WeatherSource, Weather> {
    override fun map(source: WeatherSource): Weather {
        val current = source.weatherResponse.currentWeather
        val todayDaily = source.weatherResponse.daily.firstOrNull()
        val currentWeatherCondition = current.weather.firstOrNull()
        val currentUnit = temperaturePrefs.getCurrentUnit(temperaturePrefs.state.value)


        return Weather(
            cityName = "",
            unit = currentUnit,
            timezone = source.weatherResponse.timeZone,
            currentTemp = current.temp.roundToInt(),
            feelsLikeTemp = current.feelsLike.roundToInt(),
            conditionText = currentWeatherCondition?.description?.capitalizeFirst().orEmpty(),
            iconCode = currentWeatherCondition?.icon.orEmpty(),
            minTempToday = todayDaily?.temp?.min?.roundToInt() ?: 0,
            maxTempToday = todayDaily?.temp?.max?.roundToInt() ?: 0,
            windSpeed = current.windSpeed,
            windGust = current.windGust,
            windDirectionDegrees = current.windDegree,
            humidity = current.humidity,
            pressure = current.pressure,
            uvIndex = current.uv,
            dewPoint = current.dewPoint.roundToInt(),
            sunrise = Instant.fromEpochSeconds(current.sunrise),
            sunset = Instant.fromEpochSeconds(current.sunset),
            hourlyForecast = source.hourlyForecast,
            dailyForecast = source.dailyForecast
        )
    }
}