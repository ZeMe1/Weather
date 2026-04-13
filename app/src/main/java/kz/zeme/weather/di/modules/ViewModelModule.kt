package kz.zeme.weather.di.modules

import kz.zeme.weather.presentation.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(getWeatherUseCase = get()) }
}