package kz.zeme.weather.core.preference.base

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.cancellation.CancellationException

abstract class BasePreferences<T>(
    protected val dataStore: PrefsDataStore,
    protected val scope: CoroutineScope
) {
    abstract val key: Preferences.Key<T>
    abstract val initialValue: T

    protected open fun onRead(value: T) {}
    protected open fun onSave(value: T) {}

    val flow: Flow<T> =
        dataStore.data
            .catch { e ->
                if (e is CancellationException) throw e
                emit(emptyPreferences())
            }
            .map { prefs -> prefs[key] ?: initialValue }
            .onEach { onRead(it) }

    val state: StateFlow<T> =
        flow.stateIn(scope, SharingStarted.Companion.Eagerly, initialValue)

    open suspend fun save(value: T) {
        onSave(value)
        dataStore.edit { prefs -> prefs[key] = value }
    }

    open suspend fun clear() {
        dataStore.edit { prefs -> prefs.remove(key) }
    }
}