package kz.zeme.weather.core.preference.base

import android.content.Context

fun prefsDataStorePath(context: Context): String =
    context.filesDir.resolve(dataStoreFileName).absolutePath