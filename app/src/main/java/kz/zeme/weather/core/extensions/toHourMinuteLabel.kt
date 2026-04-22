package kz.zeme.weather.core.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toHourMinuteLabel(timezone: String): String {
    val local = toLocalDateTime(TimeZone.of(timezone))
    return "%02d:%02d".format(local.hour, local.minute)
}