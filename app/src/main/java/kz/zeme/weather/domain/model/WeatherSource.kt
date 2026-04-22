package kz.zeme.weather.domain.model

import kz.zeme.weather.data.remote.dto.WeatherResponseDto

data class WeatherSource(
    val weatherResponse: WeatherResponseDto,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)
