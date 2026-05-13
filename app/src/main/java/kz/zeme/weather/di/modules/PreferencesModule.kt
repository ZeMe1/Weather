package kz.zeme.weather.di.modules

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kz.zeme.weather.core.preference.TemperatureUnitPreferences
import kz.zeme.weather.core.preference.base.PrefsDataStore
import kz.zeme.weather.core.preference.base.createDataStore
import kz.zeme.weather.core.preference.base.prefsDataStorePath
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {
    single(named("prefs_scope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    single<PrefsDataStore> {
        createDataStore { prefsDataStorePath(androidContext()) }
    }
    single { TemperatureUnitPreferences(dataStore = get(), scope = get(named("prefs_scope"))) }
}