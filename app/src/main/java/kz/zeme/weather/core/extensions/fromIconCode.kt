package kz.zeme.weather.core.extensions

import kz.zeme.weather.shared.resources.R

fun String.fromIconCode(): Int {
    return when (this) {
        "01d" -> R.drawable.ic_sun
        "01n" -> R.drawable.ic_moon2
        "02d", "03d", "04d" -> R.drawable.ic_cloud
        "02n", "03n", "04n" -> R.drawable.ic_moon
        "09d", "09n", "10d", "10n" -> R.drawable.ic_rain
        // "11d", "11n" -> TODO No thunder for now, ask in the next lesson about it
        // "13d", "13n" -> TODO No snow for now, ask in the next lesson about it
        // "50d", "50n" -> TODO No mist for now, ask in the next lesson about it
        else -> R.drawable.ic_sun
    }
}