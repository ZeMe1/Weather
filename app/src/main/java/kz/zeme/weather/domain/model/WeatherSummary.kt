package kz.zeme.weather.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import kz.zeme.weather.shared.resources.R

@Immutable
data class WeatherSummary(
    val id: Int = 0,
    val cityName: String,
    val weatherDescription: String,
    val lastFetchedTime: String,
    val weatherTemp: String,
    val minWeatherTemp: String,
    val maxWeatherTemp: String,
    @DrawableRes val backgroundRes: Int
)

val listOfDummyWeatherSummary = listOf(
    WeatherSummary(
        id = 1,
        cityName = "Almaty",
        weatherDescription = "Partly cloudy",
        lastFetchedTime = "12:35",
        weatherTemp = "11",
        minWeatherTemp = "5",
        maxWeatherTemp = "11",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 2,
        cityName = "Aktobe",
        weatherDescription = "Sunny",
        lastFetchedTime = "17:21",
        weatherTemp = "16",
        minWeatherTemp = "7",
        maxWeatherTemp = "19",
        backgroundRes = R.drawable.ic_bg_after12
    ),
    WeatherSummary(
        id = 3,
        cityName = "Astana",
        weatherDescription = "Rainy",
        lastFetchedTime = "14:54",
        weatherTemp = "10",
        minWeatherTemp = "2",
        maxWeatherTemp = "13",
        backgroundRes = R.drawable.ic_bg_after18
    ),
    WeatherSummary(
        id = 4,
        cityName = "Almaty",
        weatherDescription = "Partly cloudy",
        lastFetchedTime = "12:36",
        weatherTemp = "11",
        minWeatherTemp = "5",
        maxWeatherTemp = "11",
        backgroundRes = R.drawable.ic_bg_after24
    ),
    WeatherSummary(
        id = 5,
        cityName = "Aktobe",
        weatherDescription = "Sunny",
        lastFetchedTime = "17:26",
        weatherTemp = "16",
        minWeatherTemp = "7",
        maxWeatherTemp = "19",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 6,
        cityName = "Astana",
        weatherDescription = "Rainy",
        lastFetchedTime = "14:53",
        weatherTemp = "10",
        minWeatherTemp = "2",
        maxWeatherTemp = "13",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 7,
        cityName = "Almaty",
        weatherDescription = "Partly cloudy",
        lastFetchedTime = "12:32",
        weatherTemp = "11",
        minWeatherTemp = "5",
        maxWeatherTemp = "11",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 8,
        cityName = "Aktobe",
        weatherDescription = "Sunny",
        lastFetchedTime = "17:22",
        weatherTemp = "16",
        minWeatherTemp = "7",
        maxWeatherTemp = "19",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 9,
        cityName = "Astana",
        weatherDescription = "Rainy",
        lastFetchedTime = "14:57",
        weatherTemp = "10",
        minWeatherTemp = "2",
        maxWeatherTemp = "13",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 10,
        cityName = "Almaty",
        weatherDescription = "Partly cloudy",
        lastFetchedTime = "12:39",
        weatherTemp = "11",
        minWeatherTemp = "5",
        maxWeatherTemp = "11",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 11,
        cityName = "Aktobe",
        weatherDescription = "Sunny",
        lastFetchedTime = "17:20",
        weatherTemp = "16",
        minWeatherTemp = "7",
        maxWeatherTemp = "19",
        backgroundRes = R.drawable.ic_bg_after6
    ),
    WeatherSummary(
        id = 12,
        cityName = "Astana",
        weatherDescription = "Rainy",
        lastFetchedTime = "14:52",
        weatherTemp = "10",
        minWeatherTemp = "2",
        maxWeatherTemp = "13",
        backgroundRes = R.drawable.ic_bg_after6
    ),
)