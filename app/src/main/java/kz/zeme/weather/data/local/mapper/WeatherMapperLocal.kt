package kz.zeme.weather.data.local.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BiMapper
import kz.zeme.weather.data.local.entity.WeatherEntity
import kz.zeme.weather.data.local.entity.WeatherForecastData
import kz.zeme.weather.domain.model.Weather

class WeatherMapperLocal(
    private val hourlyMapper: HourlyForecastMapperLocal,
    private val dailyMapper: DailyForecastMapperLocal
) : BiMapper<WeatherForecastData, Weather> {
    override fun reverse(source: Weather): WeatherForecastData {
        return WeatherForecastData(
            weather = WeatherEntity(
                id = 0,
                cityName = source.cityName,
                timezone = source.timezone,
                currentTemp = source.currentTemp,
                feelsLikeTemp = source.feelsLikeTemp,
                conditionText = source.conditionText,
                iconCode = source.iconCode,
                minTempToday = source.minTempToday,
                maxTempToday = source.maxTempToday,
                windSpeed = source.windSpeed,
                windGust = source.windGust,
                windDirectionDegrees = source.windDirectionDegrees,
                humidity = source.humidity,
                pressure = source.pressure,
                uvIndex = source.uvIndex,
                dewPoint = source.dewPoint,
                sunriseEpoch = source.sunrise.epochSeconds,
                sunsetEpoch = source.sunset.epochSeconds
            ),
            hourlyForecasts = source.hourlyForecast.map { hourlyMapper.reverse(it) },
            dailyForecasts = source.dailyForecast.map { dailyMapper.reverse(it) }
        )
    }

    override fun map(source: WeatherForecastData): Weather {
        return Weather(
            cityName = source.weather.cityName,
            timezone = source.weather.timezone,
            currentTemp = source.weather.currentTemp,
            feelsLikeTemp = source.weather.feelsLikeTemp,
            conditionText = source.weather.conditionText,
            iconCode = source.weather.iconCode,
            minTempToday = source.weather.minTempToday,
            maxTempToday = source.weather.maxTempToday,
            windSpeed = source.weather.windSpeed,
            windGust = source.weather.windGust,
            windDirectionDegrees = source.weather.windDirectionDegrees,
            humidity = source.weather.humidity,
            pressure = source.weather.pressure,
            uvIndex = source.weather.uvIndex,
            dewPoint = source.weather.dewPoint,
            sunrise = Instant.fromEpochSeconds(source.weather.sunriseEpoch),
            sunset = Instant.fromEpochSeconds(source.weather.sunsetEpoch),
            hourlyForecast = source.hourlyForecasts.map { hourlyMapper.map(it) },
            dailyForecast = source.dailyForecasts.map { dailyMapper.map(it) }
        )
    }
}
