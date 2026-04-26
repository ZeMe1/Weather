package kz.zeme.weather.presentation.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kz.zeme.weather.core.architecture.State
import kz.zeme.weather.core.commonComponents.commonWidgets.LoadingScreen
import kz.zeme.weather.core.commonComponents.commonWidgets.WeatherSummaryCard
import kz.zeme.weather.domain.model.listOfDummyWeatherSummary
import kz.zeme.weather.presentation.locations.components.LocationsScreenTopBar
import kz.zeme.weather.presentation.locations.components.SearchBar
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

    when(val currentState = stateWrapper) {
        is State.Success -> {
            LoadingScreen(isLoading = currentState.data.isLoading) {
                val onIntent: (LocationsIntent) -> Unit = remember { viewModel::acceptIntent }
                LocationsScreenContent(
                    uiState = currentState.data,
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
}

@Composable
private fun LocationsScreenContent(
    uiState: LocationsState,
    onIntent: (LocationsIntent) -> Unit
) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WeatherTheme.colors.backgroundBlue)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
                .padding(top = 96.dp),
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallMediumPadding),
        ) {
            item(
                key = "main_card",
                contentType = "MainWeatherSummary"
            ) {
                WeatherSummaryCard(
                    weatherSummary = listOfDummyWeatherSummary[0],
                    isMain = true
                )
            }
            items(
                items = listOfDummyWeatherSummary,
                key = { it.id },
                contentType = { _ -> "WeatherSummaryCard" }
            ) { weatherSummary ->
                WeatherSummaryCard(
                    weatherSummary = weatherSummary,
                    isMain = false
                )
            }
            item(
                key = "info_text",
            ) {
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
        LocationsScreenTopBar(
            modifier = Modifier
                .systemBarsPadding()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
                .padding(top = WeatherTheme.dimensions.smallMediumPadding),
            onMoreClick = { onIntent(LocationsIntent.ShowBottomSheet) }
        )
        SearchBar(
            value = uiState.textFieldValue,
            onValueChange = { onIntent(LocationsIntent.ChangeTextFieldValue(it)) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}