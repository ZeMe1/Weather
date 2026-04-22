package kz.zeme.weather.domain.useCase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.repository.WeatherRepository
import kotlin.math.roundToInt

class GetHistoryWeatherAverageTempUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(coordinates: Coordinates): Result<Int> =
        runCatching {
            coroutineScope {
                val now = Clock.System.now()

                val historicalTemps = (1..YEARS_TO_FETCH)
                    .map { yearsAgo ->
                        async {
                            val timestamp = now
                                .minus(yearsAgo, DateTimeUnit.YEAR, TimeZone.UTC)
                                .epochSeconds

                            repository
                                .getHistoryWeather(coordinates, timestamp)
                                .getOrThrow()
                                .temp
                        }
                    }
                    .awaitAll()

                historicalTemps
                    .average()
                    .roundToInt()
            }
        }

    companion object {
        private const val YEARS_TO_FETCH = 3
    }
}