package kz.zeme.weather.data.local.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BiMapper
import kz.zeme.weather.data.local.entity.DailyForecastEntity
import kz.zeme.weather.domain.model.DailyForecast

class DailyForecastMapperLocal : BiMapper<DailyForecastEntity, DailyForecast> {
    override fun reverse(source: DailyForecast): DailyForecastEntity {
        return DailyForecastEntity(
            timeEpoch = source.time.epochSeconds,
            minTemp = source.minTemp,
            maxTemp = source.maxTemp,
            iconCode = source.iconCode,
            summary = source.summary,
            sunriseEpoch = source.sunrise.epochSeconds,
            sunsetEpoch = source.sunset.epochSeconds
        )
    }

    override fun map(source: DailyForecastEntity): DailyForecast {
        return DailyForecast(
            time = Instant.fromEpochSeconds(source.timeEpoch),
            minTemp = source.minTemp,
            maxTemp = source.maxTemp,
            iconCode = source.iconCode,
            summary = source.summary,
            sunrise = Instant.fromEpochSeconds(source.sunriseEpoch),
            sunset = Instant.fromEpochSeconds(source.sunsetEpoch)
        )
    }
}