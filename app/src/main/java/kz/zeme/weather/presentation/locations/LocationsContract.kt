package kz.zeme.weather.presentation.locations

import androidx.compose.runtime.Immutable
import kz.zeme.weather.core.preference.TemperatureUnit
import kz.zeme.weather.domain.model.City
import kz.zeme.weather.domain.model.WeatherSummary

@Immutable
data class LocationsState(
    val listOfWeatherSummaries: List<WeatherSummary> = emptyList(),
    val listOfSearchSuggestions: List<City> = emptyList(),
    val textFieldValue: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val isBottomSheetVisible: Boolean = false
)

sealed interface LocationsIntent {
    data class ChangeTextFieldValue(val value: String) : LocationsIntent
    data class SelectWeatherSummary(val summary: WeatherSummary) : LocationsIntent
    data class SelectLocation(val city: City) : LocationsIntent
    data class SelectTemperatureUnit(val unit: TemperatureUnit) : LocationsIntent
    data object StartSearch : LocationsIntent
    data object CancelSearch : LocationsIntent
    data object ShowBottomSheet : LocationsIntent
    data object HideBottomSheet : LocationsIntent
}

sealed interface LocationsAction {
    data object LoadScreen : LocationsAction
}

sealed interface LocationsMsg {
    data class LocationsLoaded(val locations: List<WeatherSummary>) : LocationsMsg
    data class SearchSuggestionsLoaded(val suggestions: List<City>) : LocationsMsg
    data class TextFieldValueChanged(val value: String) : LocationsMsg
    data class Loading(val isLoading: Boolean) : LocationsMsg
    data object SearchStarted : LocationsMsg
    data object SearchCancelled : LocationsMsg
    data object ClearSuggestions : LocationsMsg
    data object BottomSheetShown : LocationsMsg
    data object BottomSheetHidden : LocationsMsg
}

sealed interface LocationsLabel {
    data class ShowError(val message: Int) : LocationsLabel
    data class ShowSuccess(val message: Int) : LocationsLabel
    data class NavigateToHome(val lat: Double, val lon: Double) : LocationsLabel
}