package kz.zeme.weather.presentation.locations

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.zeme.weather.core.architecture.BaseViewModel
import kz.zeme.weather.core.architecture.Bootstrapper
import kz.zeme.weather.core.architecture.CoroutineBootstrapper
import kz.zeme.weather.core.architecture.DefaultMiddleware
import kz.zeme.weather.core.architecture.Reducer
import kz.zeme.weather.core.architecture.State
import kz.zeme.weather.core.architecture.State.Success
import kz.zeme.weather.core.architecture.StateHolder.Companion.generateOrElse

private typealias LocationsMiddlewareType = DefaultMiddleware<LocationsIntent, LocationsAction, State<LocationsState>, LocationsMsg, LocationsLabel>

class LocationsViewModel(
) : BaseViewModel<State<LocationsState>, LocationsIntent, LocationsAction, LocationsMsg, LocationsLabel>() {

    override val bootstrapper: Bootstrapper<LocationsAction> = LocationsBootstrapper()
    override val middleware: LocationsMiddlewareType = LocationsMiddleware()
    override val reducer: Reducer<State<LocationsState>, LocationsMsg> = LocationsReducer()

    private val _states = MutableStateFlow<State<LocationsState>>(Success(LocationsState()))
    override val states: StateFlow<State<LocationsState>> = _states

    private inner class LocationsReducer : Reducer<State<LocationsState>, LocationsMsg> {
        override fun reduce(state: State<LocationsState>, message: LocationsMsg): State<LocationsState> = when(message) {
            is LocationsMsg.TextFieldValueChanged -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(textFieldValue = message.value)) },
                block = { copy(data = data.copy(textFieldValue = message.value)) }
            )
            is LocationsMsg.Loading -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isLoading = message.isLoading)) },
                block = { copy(data = data.copy(isLoading = message.isLoading)) }
            )
            LocationsMsg.BottomSheetShown -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isBottomSheetVisible = true)) },
                block = { copy(data = data.copy(isBottomSheetVisible = true)) }
            )
            LocationsMsg.BottomSheetHidden -> generateOrElse<State<LocationsState>, Success<LocationsState>>(
                fallback = { Success(LocationsState(isBottomSheetVisible = false)) },
                block = { copy(data = data.copy(isBottomSheetVisible = false)) }
            )
        }
    }

    private inner class LocationsMiddleware : LocationsMiddlewareType(state = { state }, scope = viewModelScope) {
        override fun handleIntent(intent: LocationsIntent, state: () -> State<LocationsState>) {
            when(intent) {
                is LocationsIntent.ChangeTextFieldValue -> dispatch(LocationsMsg.TextFieldValueChanged(intent.value))
                LocationsIntent.ShowBottomSheet -> dispatch(LocationsMsg.BottomSheetShown)
                LocationsIntent.HideBottomSheet -> dispatch(LocationsMsg.BottomSheetHidden)
            }
        }

        override fun handleAction(action: LocationsAction, getState: () -> State<LocationsState>) {
            when (action) {
                LocationsAction.LoadScreen -> observeLocationsData()
            }
        }

        private fun observeLocationsData() {
            scope.launch(Dispatchers.IO) {
                dispatch(LocationsMsg.Loading(true))
                // some logic
                dispatch(LocationsMsg.Loading(false))
            }
        }
    }

    private inner class LocationsBootstrapper : CoroutineBootstrapper<LocationsAction>() {
        override fun invoke() {
            dispatch(LocationsAction.LoadScreen)
        }
    }
}