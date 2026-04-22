package kz.zeme.weather.core.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toDayLabel(timezone: String): String {
    return toLocalDateTime(TimeZone.of(timezone))
        .toJavaLocalDateTime()
        .dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
        .replaceFirstChar { it.uppercaseChar() }
}