package kz.zeme.weather.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kz.zeme.weather.core.architecture.BaseViewModel
import kz.zeme.weather.core.architecture.Bootstrapper
import kz.zeme.weather.core.architecture.CoroutineBootstrapper
import kz.zeme.weather.core.architecture.DefaultMiddleware
import kz.zeme.weather.core.architecture.Reducer
import kz.zeme.weather.core.architecture.State
import kz.zeme.weather.core.architecture.State.Success
import kz.zeme.weather.core.architecture.StateHolder.Companion.generateOrElse
import kz.zeme.weather.core.architecture.getOrNull
import kz.zeme.weather.core.extensions.UiText
import kz.zeme.weather.core.extensions.ensureUnit
import kz.zeme.weather.core.extensions.toDayLabel
import kz.zeme.weather.core.extensions.toErrorLabel
import kz.zeme.weather.core.extensions.toHourLabel
import kz.zeme.weather.core.extensions.toHourMinuteLabel
import kz.zeme.weather.core.navigation.destination.WeatherDestinations
import kz.zeme.weather.core.preference.TemperatureUnitPreferences
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.domain.model.DailyForecastUiItem
import kz.zeme.weather.domain.model.HourlyForecastUiItem
import kz.zeme.weather.domain.model.Weather
import kz.zeme.weather.domain.useCase.GetWeatherUseCase
import kz.zeme.weather.shared.resources.R

private typealias HomeMiddlewareType = DefaultMiddleware<HomeIntent, HomeAction, State<HomeState>, HomeMsg, HomeLabel>

class HomeViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val temperaturePrefs: TemperatureUnitPreferences,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<State<HomeState>, HomeIntent, HomeAction, HomeMsg, HomeLabel>() {

    override val bootstrapper: Bootstrapper<HomeAction> = HomeBootstrapper()
    override val middleware: HomeMiddlewareType = HomeMiddleware()
    override val reducer: Reducer<State<HomeState>, HomeMsg> = HomeReducer()

    private val args = savedStateHandle.toRoute<WeatherDestinations.Main>()
    private val lat = args.lat
    private val lon = args.lon

    private val _states = MutableStateFlow<State<HomeState>>(Success(HomeState()))
    override val states: StateFlow<State<HomeState>> = _states

    private inner class HomeReducer : Reducer<State<HomeState>, HomeMsg> {
        override fun reduce(state: State<HomeState>, message: HomeMsg): State<HomeState> = when (message) {
                is HomeMsg.WeatherDataLoaded -> generateOrElse<State<HomeState>, Success<HomeState>>(
                    fallback = { Success(HomeState(weatherData = message.weather, listOfDailyForecastItems = message.dailyForecastItems, listOfHourlyForecastItems = message.hourlyForecastItems, isLoading = false, isRefreshing = false)) },
                    block = { copy(data = data.copy(weatherData = message.weather, listOfDailyForecastItems = message.dailyForecastItems, listOfHourlyForecastItems = message.hourlyForecastItems, isLoading = false, isRefreshing = false)) }
                )
                is HomeMsg.HistoricalAverageTempLoaded -> generateOrElse<State<HomeState>, Success<HomeState>>(
                    fallback = { Success(HomeState(historicalAverageTemp = message.temp)) },
                    block = { copy(data = data.copy(historicalAverageTemp = message.temp)) }
                )
                is HomeMsg.Loading -> generateOrElse<State<HomeState>, Success<HomeState>>(
                    fallback = { Success(HomeState(isLoading = message.isLoading)) },
                    block = { copy(data = data.copy(isLoading = message.isLoading)) }
                )
                is HomeMsg.Refreshing -> generateOrElse<State<HomeState>, Success<HomeState>>(
                    fallback = { Success(HomeState(isRefreshing = message.isRefreshing)) },
                    block = { copy(data = data.copy(isRefreshing = message.isRefreshing)) }
                )
            }
    }

    private inner class HomeMiddleware : HomeMiddlewareType(state = { state }, scope = viewModelScope) {

        private val rawWeatherResult = MutableStateFlow<Result<Weather>?>(null)

        override fun handleIntent(intent: HomeIntent, state: () -> State<HomeState>) {
            when (intent) {
                HomeIntent.RefreshWeather -> observeWeatherData(isRefresh = true)
            }
        }

        override fun handleAction(action: HomeAction, getState: () -> State<HomeState>) {
            when (action) {
                HomeAction.LoadScreen -> {
                    observeWeatherData()
                    observeUnitChanges()
                }
            }
        }

        private fun resolveWeatherFlow(): Flow<Result<Weather>> =
            if (lat != null && lon != null) {
                getWeatherUseCase.invoke(lat, lon)
            } else {
                getWeatherUseCase.invoke()
            }

        private fun observeUnitChanges() {
            scope.launch {
                combine(
                    rawWeatherResult.filterNotNull(),
                    temperaturePrefs.unitFlow
                ) { result, unit ->
                    result.map { it.ensureUnit(unit) }
                }.collect { result ->
                    result.onSuccess { weatherData ->
                        dispatch(
                            HomeMsg.WeatherDataLoaded(
                                weather = weatherData,
                                hourlyForecastItems = createHourlyUiItems(weatherData),
                                dailyForecastItems = createDailyUiItems(weatherData)
                            )
                        )
                    }
                }
            }
        }

        private fun observeWeatherData(isRefresh: Boolean = false) {
            scope.launch(Dispatchers.IO) {
                if (isRefresh) dispatch(HomeMsg.Refreshing(true))

                resolveWeatherFlow().collect { result ->
                    result.onSuccess {
                        val hasData = state.getOrNull()?.weatherData != null
                        if (!hasData && !isRefresh) dispatch(HomeMsg.Loading(true))
                        rawWeatherResult.value = result
                    }.onFailure { error ->
                        if (error is LocationException.PermissionDenied) {
                            if (isRefresh) dispatch(HomeMsg.Refreshing(false))
                            else dispatch(HomeMsg.Loading(false))
                            publish(HomeLabel.RequestLocationPermission)
                        } else {
                            publish(HomeLabel.ShowError(error.toErrorLabel()))
                            dispatch(HomeMsg.Loading(false))
                            if (isRefresh) dispatch(HomeMsg.Refreshing(false))
                        }
                    }
                }
            }
        }

        private fun createHourlyUiItems(weather: Weather): List<HourlyForecastUiItem> {
            val timezone = weather.timezone
            val now = Clock.System.now()
            val windowEnd = weather.hourlyForecast.lastOrNull()?.time ?: now

            val hourlyForecastItems = weather.hourlyForecast.mapIndexed { index, forecast ->
                val label = if (index == 0) {
                    UiText.Resource(R.string.now)
                } else {
                    UiText.DynamicString(forecast.time.toHourLabel(timezone))
                }

                forecast.time to HourlyForecastUiItem.Forecast(
                    label = label,
                    iconCode = forecast.iconCode,
                    temp = forecast.temp
                )
            }

            val sunEvents = weather.dailyForecast.flatMap { daily ->
                listOf(
                    daily.sunrise to HourlyForecastUiItem.SunEvent(
                        timeLabel = UiText.DynamicString(daily.sunrise.toHourMinuteLabel(timezone)),
                        isSunrise = true
                    ),
                    daily.sunset to HourlyForecastUiItem.SunEvent(
                        timeLabel = UiText.DynamicString(daily.sunset.toHourMinuteLabel(timezone)),
                        isSunrise = false
                    )
                )
            }.filter { (time, _) -> time in (now..windowEnd) }

            return (hourlyForecastItems + sunEvents)
                .sortedBy { (time, _) -> time }
                .map { (_, item) -> item }
        }

        private fun createDailyUiItems(weather: Weather): List<DailyForecastUiItem> {
            return weather.dailyForecast.mapIndexed { index, forecast ->
                val dayLabel = if (index == 0) {
                    UiText.Resource(R.string.today)
                } else {
                    UiText.DynamicString(forecast.time.toDayLabel(weather.timezone))
                }

                DailyForecastUiItem(
                    dayLabel = dayLabel,
                    iconCode = forecast.iconCode,
                    minTemp = forecast.minTemp,
                    maxTemp = forecast.maxTemp,
                    summary = forecast.summary
                )
            }
        }
    }

    private inner class HomeBootstrapper : CoroutineBootstrapper<HomeAction>() {
        override fun invoke() {
            dispatch(HomeAction.LoadScreen)
        }
    }
}