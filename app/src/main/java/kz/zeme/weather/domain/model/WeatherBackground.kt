package kz.zeme.weather.domain.model

import androidx.annotation.DrawableRes
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kz.zeme.weather.shared.resources.R

enum class WeatherBackground(@DrawableRes val drawableRes: Int) {
    MORNING_CLEAR(R.drawable.ic_bg_after6),
    DAY_CLEAR(R.drawable.ic_bg_after12),
    EVENING_CLEAR(R.drawable.ic_bg_after18),
    NIGHT_CLEAR(R.drawable.ic_bg_after24),
    MORNING_RAIN(R.drawable.ic_bg_after6_rain),
    DAY_RAIN(R.drawable.ic_bg_after12_rain),
    EVENING_RAIN(R.drawable.ic_bg_after18_rain),
    NIGHT_RAIN(R.drawable.ic_bg_after24_rain),
}

fun resolveWeatherBackground(
    iconCode: String,
    timezone: String
): WeatherBackground {
    val localHour = currentLocalHour(timezone)
    val isRain = isRainyCondition(iconCode)

    return when (localHour) {
        in 6..11 if !isRain -> WeatherBackground.MORNING_CLEAR
        in 12..17 if !isRain -> WeatherBackground.DAY_CLEAR
        in 18..23 if !isRain -> WeatherBackground.EVENING_CLEAR
        in 0..5 if !isRain -> WeatherBackground.NIGHT_CLEAR
        in 6..11 if isRain -> WeatherBackground.MORNING_RAIN
        in 12..17 if isRain -> WeatherBackground.DAY_RAIN
        in 18..23 if isRain -> WeatherBackground.EVENING_RAIN
        else -> WeatherBackground.NIGHT_RAIN
    }
}

private fun currentLocalHour(timezone: String): Int {
    val tz = TimeZone.of(timezone)
    val now = kotlinx.datetime.Clock.System.now()
    return now.toLocalDateTime(tz).hour
}

private fun isRainyCondition(iconCode: String): Boolean {
    val code = iconCode.dropLast(1)
    return code in setOf("09", "10", "11")
}