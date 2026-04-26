package kz.zeme.weather.presentation.locations

import androidx.compose.runtime.Immutable
import kz.zeme.weather.domain.model.WeatherSummary

@Immutable
data class LocationsState(
    val listOfWeatherSummaries: List<WeatherSummary> = emptyList(),
    val textFieldValue: String = "",
    val isLoading: Boolean = false,
    val isBottomSheetVisible: Boolean = false
)

sealed interface LocationsIntent {
    data class ChangeTextFieldValue(val value: String) : LocationsIntent
    data object ShowBottomSheet : LocationsIntent
    data object HideBottomSheet : LocationsIntent
}

sealed interface LocationsAction {
    data object LoadScreen : LocationsAction
}

sealed interface LocationsMsg {
    data class TextFieldValueChanged(val value: String) : LocationsMsg
    data class Loading(val isLoading: Boolean) : LocationsMsg
    data object BottomSheetShown : LocationsMsg
    data object BottomSheetHidden : LocationsMsg
}

sealed interface LocationsLabel {
    data class ShowError(val message: Int) : LocationsLabel
}