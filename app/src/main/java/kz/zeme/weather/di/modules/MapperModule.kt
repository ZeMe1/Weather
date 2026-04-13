package kz.zeme.weather.di.modules

import kz.zeme.weather.data.mapper.WeatherMapper
import org.koin.dsl.module

val mapperModule = module {
    factory { WeatherMapper() }
}