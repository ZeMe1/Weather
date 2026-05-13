package kz.zeme.weather.core.extensions

import kz.zeme.weather.core.preference.TemperatureUnit
import kz.zeme.weather.domain.model.Weather
import kotlin.math.roundToInt

fun Weather.ensureUnit(targetUnit: TemperatureUnit): Weather {
    if (this.unit == targetUnit) return this
    val isToFahrenheit = targetUnit == TemperatureUnit.FAHRENHEIT

    fun convert(temp: Double): Double =
        if (isToFahrenheit) (temp * 1.8) + 32 else (temp - 32) / 1.8

    return this.copy(
        unit = targetUnit,
        currentTemp = convert(currentTemp.toDouble()).roundToInt(),
        feelsLikeTemp = convert(feelsLikeTemp.toDouble()).roundToInt(),
        minTempToday = convert(minTempToday.toDouble()).roundToInt(),
        maxTempToday = convert(maxTempToday.toDouble()).roundToInt(),
        hourlyForecast = hourlyForecast.map { it.copy(temp = convert(it.temp.toDouble()).roundToInt()) },
        dailyForecast = dailyForecast.map { it.copy(minTemp = convert(it.minTemp.toDouble()).roundToInt(), maxTemp = convert(it.maxTemp.toDouble()).roundToInt()) }
    )
}