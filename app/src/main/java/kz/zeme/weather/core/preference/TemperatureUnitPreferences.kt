package kz.zeme.weather.core.preference

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zeme.weather.core.preference.base.BasePreferences
import kz.zeme.weather.core.preference.base.PrefsDataStore

class TemperatureUnitPreferences(
    dataStore: PrefsDataStore,
    scope: CoroutineScope
) : BasePreferences<String>(dataStore, scope) {

    override val key: Preferences.Key<String> = stringPreferencesKey("temperature_unit")
    override val initialValue: String = TemperatureUnit.CELSIUS.name

    val unitFlow: Flow<TemperatureUnit> = flow.map { getCurrentUnit(it) }

    suspend fun setUnit(unit: TemperatureUnit) = save(unit.name)

    fun getCurrentUnit(raw: String?): TemperatureUnit =
        runCatching { TemperatureUnit.valueOf(raw ?: initialValue) }
            .getOrDefault(TemperatureUnit.CELSIUS)
}