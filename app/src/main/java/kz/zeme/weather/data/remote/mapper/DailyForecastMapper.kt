package kz.zeme.weather.data.remote.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BaseMapper
import kz.zeme.weather.data.remote.dto.DailyDto
import kz.zeme.weather.domain.model.DailyForecast
import kotlin.math.roundToInt

class DailyForecastMapper: BaseMapper<DailyDto, DailyForecast> {
    override fun map(source: DailyDto): DailyForecast {
        return DailyForecast(
            time = Instant.fromEpochSeconds(source.dateTime),
            minTemp = source.temp.min.roundToInt(),
            maxTemp = source.temp.max.roundToInt(),
            iconCode = source.weather.firstOrNull()?.icon.orEmpty(),
            summary = source.summary,
            sunrise = Instant.fromEpochSeconds(source.sunrise),
            sunset = Instant.fromEpochSeconds(source.sunset)
        )
    }
}
