package kz.zeme.weather.presentation.locations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import kz.zeme.weather.core.architecture.LaunchedEffectLifecycle
import kz.zeme.weather.core.architecture.State
import kz.zeme.weather.core.commonComponents.commonWidgets.LoadingScreen
import kz.zeme.weather.core.commonComponents.commonWidgets.WeatherSummaryCard
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.core.navigation.destination.WeatherDestinations.Main
import kz.zeme.weather.core.snackbar.LocalWeatherSnackBarHostState
import kz.zeme.weather.core.snackbar.WeatherSnackBarHost
import kz.zeme.weather.presentation.locations.components.LocationsScreenTopBar
import kz.zeme.weather.presentation.locations.components.SearchBar
import kz.zeme.weather.presentation.locations.components.SearchSuggestionResultRow
import kz.zeme.weather.presentation.locations.components.SettingsBottomSheet
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsScreen(
    navController: NavHostController,
    viewModel: LocationsViewModel = koinViewModel()
) {
    val stateWrapper by viewModel.states.collectAsStateWithLifecycle()
    val snackBarHostState = LocalWeatherSnackBarHostState.current

    when (val currentState = stateWrapper) {
        is State.Success -> {
            LoadingScreen(isLoading = currentState.data.isLoading) {
                val onIntent: (LocationsIntent) -> Unit = remember { viewModel::acceptIntent }
                LocationsScreenContent(
                    uiState = currentState.data,
                    navController = navController,
                    onIntent = onIntent
                )
            }
            SettingsBottomSheet(
                isVisible = currentState.data.isBottomSheetVisible,
                onIntent = viewModel::acceptIntent
            )
        }

        else -> {}
    }

    LaunchedEffectLifecycle(key = viewModel, lifecycleState = Lifecycle.State.STARTED) {
        viewModel.labels.collectLatest { label ->
            when (label) {
                is LocationsLabel.NavigateToHome -> {
                    navController.navigate(Main(lat = label.lat, lon = label.lon))
                }
                is LocationsLabel.ShowSuccess -> {
                    snackBarHostState.show(messageRes = label.message)
                }
                is LocationsLabel.ShowError -> {}
            }

        }
    }
    WeatherSnackBarHost(
        hostState = snackBarHostState,
        modifier = Modifier.padding(top = WeatherTheme.dimensions.extraLargePadding)
    )
}

@Composable
private fun LocationsScreenContent(
    uiState: LocationsState,
    navController: NavHostController,
    onIntent: (LocationsIntent) -> Unit
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WeatherTheme.colors.backgroundBlue)
    ) {

        LocationsScreenTopBar(
            modifier = Modifier
                .systemBarsPadding()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
                .padding(top = WeatherTheme.dimensions.smallMediumPadding),
            onMoreClick = { onIntent(LocationsIntent.ShowBottomSheet) }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
                .padding(top = 96.dp)
                .then(
                    if (uiState.isSearching) Modifier.blur(
                        radius = 8.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    else Modifier
                ),
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallMediumPadding),
        ) {
            uiState.listOfWeatherSummaries.firstOrNull()?.let { mainSummary ->
                item(key = "main_card", contentType = "MainWeatherSummary") {
                    WeatherSummaryCard(
                        weatherSummary = mainSummary,
                        isMain = true,
                        onClick = { onIntent(LocationsIntent.SelectWeatherSummary(mainSummary)) }
                    )
                }
            }
            items(
                items = uiState.listOfWeatherSummaries.drop(1),
                key = { "weather_${it.id}" },
                contentType = { "WeatherSummaryCard" }
            ) { summary ->
                WeatherSummaryCard(
                    weatherSummary = summary,
                    isMain = false,
                    onClick = { onIntent(LocationsIntent.SelectWeatherSummary(summary)) }
                )
            }
            item(key = "info_text") {
                Text(
                    text = stringResource(R.string.meteo_map_data_detailed),
                    style = WeatherTheme.typography.weight500Size10LineHeight10,
                    color = WeatherTheme.colors.textWhite.copy(0.5f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(WeatherTheme.dimensions.largePadding))
            }
        }

        AnimatedVisibility(
            visible = uiState.isSearching,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {}
                    .background(WeatherTheme.colors.backgroundBlue.copy(alpha = 0.85f))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 64.dp)
                ) {
                    if (uiState.listOfSearchSuggestions.isEmpty() && uiState.textFieldValue.isNotBlank()) {
                        item {
                            Text(
                                text = stringResource(R.string.search_no_result),
                                style = WeatherTheme.typography.weight500Size20LineHeight20,
                                color = WeatherTheme.colors.textWhite.copy(0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 100.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(
                            items = uiState.listOfSearchSuggestions,
                            key = { index, city -> "${city.latitude}_${city.longitude}_$index" }
                        ) { index, city ->
                            SearchSuggestionResultRow(
                                city = city,
                                onClick = { onIntent(LocationsIntent.SelectLocation(city)) }
                            )
                        }
                    }
                }
            }
        }

        SearchBar(
            value = uiState.textFieldValue,
            onValueChange = { onIntent(LocationsIntent.ChangeTextFieldValue(it)) },
            onFocusGained = { onIntent(LocationsIntent.StartSearch) },
            onCancel = { onIntent(LocationsIntent.CancelSearch) },
            isSearching = uiState.isSearching,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
