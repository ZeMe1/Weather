package kz.zeme.weather.data.local.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun fromInstant(value: Long?): Instant? = value?.let { Instant.fromEpochSeconds(it) }

    @TypeConverter
    fun toInstant(instant: Instant?): Long? = instant?.epochSeconds
}