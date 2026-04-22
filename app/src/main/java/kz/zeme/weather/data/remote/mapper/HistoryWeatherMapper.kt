package kz.zeme.weather.data.remote.mapper

import kotlinx.datetime.Instant
import kz.zeme.weather.core.mapper.BaseMapper
import kz.zeme.weather.data.remote.dto.TimeMachineDataDto
import kz.zeme.weather.domain.model.HistoryWeather
import kotlin.math.roundToInt

class HistoryWeatherMapper: BaseMapper<TimeMachineDataDto, HistoryWeather> {
    override fun map(source: TimeMachineDataDto): HistoryWeather {
        return HistoryWeather(
            timeStamp = Instant.fromEpochSeconds(source.timeStamp),
            temp = source.temp.roundToInt()
        )
    }
}