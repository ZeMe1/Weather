package kz.zeme.weather.core.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toHourLabel(timezone: String): String {
    val local = toLocalDateTime(TimeZone.of(timezone))
    return local.hour.toString()
}