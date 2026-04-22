package kz.zeme.weather.data.local.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BiMapper
import kz.zeme.weather.data.local.entity.HourlyForecastEntity
import kz.zeme.weather.domain.model.HourlyForecast

class HourlyForecastMapperLocal : BiMapper<HourlyForecastEntity, HourlyForecast> {
    override fun reverse(source: HourlyForecast): HourlyForecastEntity {
        return HourlyForecastEntity(
            timeEpoch = source.time.epochSeconds,
            temp = source.temp,
            iconCode = source.iconCode
        )
    }

    override fun map(source: HourlyForecastEntity): HourlyForecast {
        return HourlyForecast(
            time = Instant.fromEpochSeconds(source.timeEpoch),
            temp = source.temp,
            iconCode = source.iconCode
        )
    }
}