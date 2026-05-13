package kz.zeme.weather.di.modules

import kz.zeme.weather.presentation.home.HomeViewModel
import kz.zeme.weather.presentation.locations.LocationsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(getWeatherUseCase = get(), temperaturePrefs = get(), savedStateHandle = get()) }
    viewModel { LocationsViewModel(getGeocodingUseCase = get(), getAllLocationsWeatherUseCase = get(), temperaturePrefs = get()) }
}