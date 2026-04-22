package kz.zeme.weather.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kz.zeme.weather.core.architecture.LaunchedEffectLifecycle
import kz.zeme.weather.core.architecture.State.Success
import kz.zeme.weather.core.commonComponents.commonWidgets.LoadingScreen
import kz.zeme.weather.core.extensions.toHourMinuteLabel
import kz.zeme.weather.core.snackbar.LocalWeatherSnackBarHostState
import kz.zeme.weather.core.snackbar.WeatherSnackBarHost
import kz.zeme.weather.presentation.home.components.AverageBlock
import kz.zeme.weather.presentation.home.components.DailyForecastBlock
import kz.zeme.weather.presentation.home.components.FeelsLikeBlock
import kz.zeme.weather.presentation.home.components.HeaderBlock
import kz.zeme.weather.presentation.home.components.HourlyForecastBlock
import kz.zeme.weather.presentation.home.components.HumidityBlock
import kz.zeme.weather.presentation.home.components.PressureBlock
import kz.zeme.weather.presentation.home.components.SunsetBlock
import kz.zeme.weather.presentation.home.components.UvIndexBlock
import kz.zeme.weather.presentation.home.components.WindBlock
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val stateWrapper by viewModel.states.collectAsStateWithLifecycle()
    val snackBarHostState = LocalWeatherSnackBarHostState.current

    when (val currentState = stateWrapper) {
        is Success -> {
            LoadingScreen(isLoading = currentState.data.isLoading) {
                HomeScreenContent(
                    uiState = currentState.data,
                    onIntent = viewModel::acceptIntent
                )
            }
        }

        else -> {

        }
    }
    LaunchedEffectLifecycle (key = viewModel, lifecycleState = Lifecycle.State.STARTED) {
        viewModel.labels.collectLatest { label ->
            when (label) {
                is HomeLabel.ShowError -> snackBarHostState.show(
                    messageRes = label.message
                )
            }
        }
    }
    WeatherSnackBarHost(
        hostState = snackBarHostState,
        modifier = Modifier.padding(top = WeatherTheme.dimensions.extraLargePadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    val weatherData = uiState.weatherData
    val scrollState = rememberScrollState()
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onIntent(HomeIntent.RefreshWeather) },
        state = refreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = WeatherTheme.dimensions.largePadding),
                isRefreshing = uiState.isRefreshing,
                state = refreshState,
                containerColor = WeatherTheme.colors.white,
                color = WeatherTheme.colors.cardCyan,

            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(uiState.backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WeatherTheme.dimensions.mediumPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(56.dp))
                    if (weatherData != null) {
                        HeaderBlock(weatherData = weatherData)

                        Spacer(Modifier.height(72.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
                        ) {
                            HourlyForecastBlock(
                                descText = uiState.listOfDailyForecastItems.firstOrNull()?.summary.orEmpty(),
                                listOfHourlyForecast = uiState.listOfHourlyForecastItems
                            )

                            DailyForecastBlock(listOfDailyForecast = uiState.listOfDailyForecastItems)

                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
                            ) {
                                AverageBlock(
                                    todayMaxTemp = weatherData.maxTempToday,
                                    averageMaxTemp = uiState.historicalAverageTemp ?: 0,
                                    modifier = Modifier.weight(1f)
                                )
                                FeelsLikeBlock(
                                    temp = weatherData.feelsLikeTemp,
                                    descText = stringResource(uiState.feelsLikeDescription),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            WindBlock(
                                windSpeed = weatherData.windSpeed.toString(),
                                windGustSpeed = weatherData.windGust.toString(),
                                windDirectionDegree = weatherData.windDirectionDegrees.toString(),
                                windDirectionLabel = stringResource(uiState.windDirectionLabel)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
                            ) {
                                UvIndexBlock(
                                    uvIndex = weatherData.uvIndex.roundToInt(),
                                    levelLabel = stringResource(uiState.uvLevelLabel),
                                    description = stringResource(uiState.uvDescriptionLabel),
                                    modifier = Modifier.weight(1f)
                                )
                                SunsetBlock(
                                    sunsetTime = weatherData.sunset.toHourMinuteLabel(weatherData.timezone),
                                    sunriseTime = weatherData.sunrise.toHourMinuteLabel(weatherData.timezone),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
                            ) {
                                HumidityBlock(
                                    humidity = "${weatherData.humidity}${SymbolConstants.PERCENTAGE}",
                                    dewPoint = weatherData.dewPoint,
                                    modifier = Modifier.weight(1f)
                                )
                                PressureBlock(
                                    pressure = weatherData.pressure,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}