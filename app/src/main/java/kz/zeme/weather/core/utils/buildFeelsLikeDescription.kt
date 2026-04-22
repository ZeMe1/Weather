package kz.zeme.weather.core.utils

import kz.zeme.weather.shared.resources.R

fun buildFeelsLikeDescription(currentTemp: Int, feelsLikeTemp: Int): Int {
    return when {
        feelsLikeTemp > currentTemp -> R.string.feels_like
        feelsLikeTemp < currentTemp -> R.string.feels_like_colder
        else -> R.string.feels_like_same
    }
}