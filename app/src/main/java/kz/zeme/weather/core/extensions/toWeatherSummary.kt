package kz.zeme.weather.core.extensions

import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.model.WeatherSummary
import kz.zeme.weather.domain.model.resolveWeatherBackground

fun Weather.toWeatherSummary(): WeatherSummary {
    val (lat, lon) = id.split("_")
        .map { it.toDoubleOrNull() ?: 0.0 }
        .let { it[0] to it[1] }

    return WeatherSummary(
        id = id.ifBlank { cityName },
        lat = lat,
        lon = lon,
        cityName = cityName,
        weatherDescription = conditionText,
        lastFetchedTime = "11:55",
        weatherTemp = currentTemp,
        minWeatherTemp = minTempToday,
        maxWeatherTemp = maxTempToday,
        backgroundRes = resolveWeatherBackground(iconCode = iconCode, timezone = timezone).drawableRes
    )
}