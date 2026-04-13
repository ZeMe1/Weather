package kz.zeme.weather.data.remote.dto

data class WeatherResponseDto(
    val name: String,
    val weather: List<WeatherDto>,
    val main: MainDto,
    val wind: WindDto,
    val sys: SysDto
)