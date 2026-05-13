package kz.zeme.weather.presentation.locations

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kz.zeme.weather.core.architecture.BaseViewModel
import kz.zeme.weather.core.architecture.Bootstrapper
import kz.zeme.weather.core.architecture.CoroutineBootstrapper
import kz.zeme.weather.core.architecture.DefaultMiddleware
import kz.zeme.weather.core.architecture.Reducer
import kz.zeme.weather.core.architecture.State
import kz.zeme.weather.core.architecture.State.Success
import kz.zeme.weather.core.architecture.StateHolder.Companion.generateOrElse
import kz.zeme.weather.core.extensions.ensureUnit
import kz.zeme.weather.core.extensions.toWeatherSummary
import kz.zeme.weather.core.preference.TemperatureUnit
import kz.zeme.weather.core.preference.TemperatureUnitPreferences
import kz.zeme.weather.domain.useCase.GetAllLocationsUseCase
import kz.zeme.weather.domain.useCase.GetGeocodingUseCase
import kz.zeme.weather.presentation.locations.LocationsLabel.NavigateToHome
import kz.zeme.weather.presentation.locations.LocationsMsg.BottomSheetHidden
import kz.zeme.weather.presentation.locations.LocationsMsg.BottomSheetShown
import kz.zeme.weather.presentation.locations.LocationsMsg.ClearSuggestions
import kz.zeme.weather.presentation.locations.LocationsMsg.Loading
import kz.zeme.weather.presentation.locations.LocationsMsg.LocationsLoaded
import kz.zeme.weather.presentation.locations.LocationsMsg.SearchCancelled
import kz.zeme.weather.presentation.locations.LocationsMsg.SearchStarted
import kz.zeme.weather.presentation.locations.LocationsMsg.SearchSuggestionsLoaded
import kz.zeme.weather.presentation.locations.LocationsMsg.TextFieldValueChanged
import kz.zeme.weather.shared.resources.R

private typealias LocationsMiddlewareType = DefaultMiddleware<LocationsIntent, LocationsAction, State<LocationsState>, LocationsMsg, LocationsLabel>

class LocationsViewModel(
    private val getGeocodingUseCase: GetGeocodingUseCase,
    private val getAllLocationsWeatherUseCase: GetAllLocationsUseCase,
    private val temperaturePrefs: TemperatureUnitPreferences
) : BaseViewModel<State<LocationsState>, LocationsIntent, LocationsAction, LocationsMsg, LocationsLabel>() {

    override val bootstrapper: Bootstrapper<LocationsAction> = LocationsBootstrapper()
    override val middleware: LocationsMiddlewareType = LocationsMiddleware()
    override val reducer: Reducer<State<LocationsState>, LocationsMsg> = LocationsReducer()

    private val _states = MutableStateFlow<State<LocationsState>>(Success(LocationsState()))
    override val states: StateFlow<State<LocationsState>> = _states

    private inner class LocationsReducer : Reducer<State<LocationsState>, LocationsMsg> {
        override fun reduce(state: State<LocationsState>, message: LocationsMsg): State<LocationsState> = when(message) {
            is LocationsLoaded -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(listOfWeatherSummaries = message.locations)) },
                block = { copy(data = data.copy(listOfWeatherSummaries = message.locations)) }
            )
            is SearchSuggestionsLoaded -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(listOfSearchSuggestions = message.suggestions)) },
                block = { copy(data = data.copy(listOfSearchSuggestions = message.suggestions)) }
            )
            is TextFieldValueChanged -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(textFieldValue = message.value)) },
                block = { copy(data = data.copy(textFieldValue = message.value)) }
            )
            is Loading -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isLoading = message.isLoading)) },
                block = { copy(data = data.copy(isLoading = message.isLoading)) }
            )
            SearchStarted -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isSearching = true)) },
                block = { copy(data = data.copy(isSearching = true)) }
            )
            SearchCancelled -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isSearching = false, listOfSearchSuggestions = emptyList())) },
                block = { copy(data = data.copy(isSearching = false, listOfSearchSuggestions = emptyList())) }
            )
            ClearSuggestions -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(listOfSearchSuggestions = emptyList())) },
                block = { copy(data = data.copy(listOfSearchSuggestions = emptyList())) }
            )
            BottomSheetShown -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isBottomSheetVisible = true)) },
                block = { copy(data = data.copy(isBottomSheetVisible = true)) }
            )
            BottomSheetHidden -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isBottomSheetVisible = false)) },
                block = { copy(data = data.copy(isBottomSheetVisible = false)) }
            )
        }
    }

    private inner class LocationsMiddleware : LocationsMiddlewareType(state = { state }, scope = viewModelScope) {
        private var searchJob: Job? = null

        override fun handleIntent(intent: LocationsIntent, state: () -> State<LocationsState>) {
            when(intent) {
                is LocationsIntent.SelectWeatherSummary -> { publish(NavigateToHome(lat = intent.summary.lat, lon = intent.summary.lon)) }
                is LocationsIntent.SelectLocation -> { publish(NavigateToHome(lat = intent.city.latitude, lon = intent.city.longitude)) }
                is LocationsIntent.SelectTemperatureUnit -> changeTempUnit(intent.unit)
                is LocationsIntent.ChangeTextFieldValue -> {
                    dispatch(TextFieldValueChanged(intent.value))

                    searchJob?.cancel()
                    if (intent.value.isBlank()) {
                        dispatch(ClearSuggestions)
                    } else {
                        searchJob = scope.launch {
                            delay(600)
                            searchCities(query = intent.value.trim())
                        }
                    }
                }
                LocationsIntent.ShowBottomSheet -> dispatch(BottomSheetShown)
                LocationsIntent.HideBottomSheet -> dispatch(BottomSheetHidden)
                LocationsIntent.StartSearch -> dispatch(SearchStarted)
                LocationsIntent.CancelSearch -> dispatch(SearchCancelled)
            }
        }

        override fun handleAction(action: LocationsAction, getState: () -> State<LocationsState>) {
            when (action) {
                LocationsAction.LoadScreen -> observeLocationsData()
            }
        }

        private fun changeTempUnit(unit: TemperatureUnit) {
            scope.launch {
                val message = when(unit) {
                    TemperatureUnit.CELSIUS -> R.string.temp_unit_changed_celsius
                    TemperatureUnit.FAHRENHEIT -> R.string.temp_unit_changed_fahrenheit
                }
                temperaturePrefs.setUnit(unit)
                publish(LocationsLabel.ShowSuccess(message = message))
                dispatch(BottomSheetHidden)
            }
        }

        private fun searchCities(query: String) {
            scope.launch(Dispatchers.IO) {
                getGeocodingUseCase(query)
                    .onSuccess { cities ->
                        dispatch(LocationsMsg.SearchSuggestionsLoaded(cities))
                    }
                    .onFailure {
                        publish(LocationsLabel.ShowError(R.string.something_went_wrong))
                    }
            }
        }

        private fun observeLocationsData() {
            scope.launch(Dispatchers.IO) {
                dispatch(LocationsMsg.Loading(true))
                combine(
                    getAllLocationsWeatherUseCase(),
                    temperaturePrefs.unitFlow
                ) { locations, unit ->
                    locations.map { weather ->
                        weather.ensureUnit(unit).toWeatherSummary()
                    }
                }.collect { summaries ->
                    dispatch(LocationsMsg.LocationsLoaded(summaries))
                    dispatch(Loading(false))
                }
            }
        }
    }

    private inner class LocationsBootstrapper : CoroutineBootstrapper<LocationsAction>() {
        override fun invoke() {
            dispatch(LocationsAction.LoadScreen)
        }
    }
}