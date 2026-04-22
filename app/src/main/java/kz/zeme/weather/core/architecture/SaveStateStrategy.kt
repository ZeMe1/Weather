package kz.zeme.weather.core.architecture

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

/**
 * Стратегия сохранения и восстановления состояния
 */
interface SaveStateStrategy<State : Any> {

    /**
     * Сохраняет состояние в SavedStateHandle
     */
    fun saveState(state: State, savedStateHandle: SavedStateHandle)

    /**
     * Восстанавливает состояние из SavedStateHandle
     * @param defaultValue - значение по умолчанию, если восстановление невозможно
     * @return восстановленное состояние или defaultValue
     */
    fun restoreState(savedStateHandle: SavedStateHandle, defaultValue: State): State

    // Функция подписки и сохранения
    suspend fun observeAndSave(stateFlow: StateFlow<State>, handler: SavedStateHandle, errorHandler: ErrorHandler) {
        stateFlow.collect { state -> runCatching { saveState(state, handler) }.onFailure(errorHandler::handleError) }
    }
}

/**
 * Стратегия, которая сохраняет и восстанавливает состояние напрямую через SavedStateHandle
 * Требует, чтобы State был Parcelable или Serializable
 */
class DirectSaveStateStrategy<State : Any>(
    private val key: String = "view_model_state"
) : SaveStateStrategy<State> {

    override fun saveState(state: State, savedStateHandle: SavedStateHandle) {
        savedStateHandle[key] = state
    }

    @Suppress("UNCHECKED_CAST")
    override fun restoreState(savedStateHandle: SavedStateHandle, defaultValue: State): State {
        return savedStateHandle.get<State>(key) ?: defaultValue
    }
}

/**
 * Стратегия, которая не сохраняет состояние (используется по умолчанию)
 */
class NoOpSaveStateStrategy<State : Any> : SaveStateStrategy<State> {
    override fun saveState(state: State, savedStateHandle: SavedStateHandle) = Unit
    override fun restoreState(savedStateHandle: SavedStateHandle, defaultValue: State): State = defaultValue
}

/**
 * Базовая стратегия для сохранения только определенных полей состояния
 */
abstract class PartialSaveStateStrategy<State : Any> : SaveStateStrategy<State> {

    override fun saveState(state: State, savedStateHandle: SavedStateHandle) {
        extractStateData(state).forEach { (key, value) -> savedStateHandle[key] = value }
    }

    /**
     * Извлекает данные из состояния для сохранения
     * @return Map<String, Any?> с ключами и значениями для сохранения
     */
    protected abstract fun extractStateData(state: State): Map<String, Any?>

    /**
     * Применяет сохраненные данные к начальному состоянию
     */
    protected abstract fun applyStateData(initialState: State, savedData: Map<String, Any?>): State

    /**
     * Восстанавливает состояние из SavedStateHandle с дефолтным значением
     */
    override fun restoreState(savedStateHandle: SavedStateHandle, defaultValue: State): State {
        val savedData = mutableMapOf<String, Any?>()

        // Проверяем наличие всех ключей и извлекаем значения
        extractStateData(defaultValue).keys.forEach { key ->
            if (savedStateHandle.contains(key)) {
                savedData[key] = savedStateHandle.get<Any>(key)
            }
        }

        return if (savedData.isNotEmpty()) applyStateData(defaultValue, savedData) else defaultValue
    }
}