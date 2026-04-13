package kz.zeme.weather.data.mapper

import kz.zeme.weather.data.remote.dto.WeatherResponseDto
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.shared.core.mapper.BaseMapper
import kotlin.math.roundToInt

class WeatherMapper: BaseMapper<WeatherResponseDto, Weather> {
    override fun map(source: WeatherResponseDto): Weather {
        val weatherCondition = source.weather.firstOrNull()

        return Weather(
            cityName = source.name,
            currentTemp = fromKelvinToCelsius(source.main.temp),
            minTemp = fromKelvinToCelsius(source.main.tempMin),
            maxTemp = fromKelvinToCelsius(source.main.tempMax),
            feelsLikeTemp = fromKelvinToCelsius(source.main.feelsLike),
            condition = weatherCondition?.main.orEmpty(),
            windSpeed = source.wind.speed,
            windDirectionDegrees = source.wind.deg,
            humidity = source.main.humidity,
            pressure = source.main.pressure,
            iconCode = weatherCondition?.icon.orEmpty(),
            sunrise = source.sys.sunrise,
            sunset = source.sys.sunset
        )
    }

    private fun fromKelvinToCelsius(kelvin: Double): Int {
        return(kelvin - KELVIN_NUMBER).roundToInt()
    }

    companion object {
        const val KELVIN_NUMBER = 273.15
    }
}