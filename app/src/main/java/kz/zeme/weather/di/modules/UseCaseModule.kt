package kz.zeme.weather.di.modules

import kz.zeme.weather.domain.useCase.GetWeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetWeatherUseCase(repository = get(), locationService = get()) }
}