package kz.zeme.weather.data.remote.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BaseMapper
import kz.zeme.weather.data.remote.dto.HourlyDto
import kz.zeme.weather.domain.model.HourlyForecast
import kotlin.math.roundToInt

class HourlyForecastMapper: BaseMapper<HourlyDto, HourlyForecast> {
    override fun map(source: HourlyDto): HourlyForecast {
        return HourlyForecast(
            time = Instant.fromEpochSeconds(source.dateTime),
            temp = source.temp.roundToInt(),
            iconCode = source.weather.firstOrNull()?.icon.orEmpty()
        )
    }
}