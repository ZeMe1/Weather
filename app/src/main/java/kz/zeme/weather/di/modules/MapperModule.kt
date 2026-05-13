package kz.zeme.weather.di.modules

import kz.zeme.weather.data.local.mapper.DailyForecastMapperLocal
import kz.zeme.weather.data.local.mapper.HourlyForecastMapperLocal
import kz.zeme.weather.data.local.mapper.WeatherMapperLocal
import kz.zeme.weather.data.remote.mapper.DailyForecastMapper
import kz.zeme.weather.data.remote.mapper.GeocodingMapper
import kz.zeme.weather.data.remote.mapper.HistoryWeatherMapper
import kz.zeme.weather.data.remote.mapper.HourlyForecastMapper
import kz.zeme.weather.data.remote.mapper.WeatherMapper
import org.koin.dsl.module

val mapperModule = module {
    factory { WeatherMapper(temperaturePrefs = get()) }
    factory { HourlyForecastMapper() }
    factory { DailyForecastMapper() }
    factory { HistoryWeatherMapper() }
    factory { HourlyForecastMapperLocal() }
    factory { DailyForecastMapperLocal() }
    factory { WeatherMapperLocal(hourlyMapper = get(), dailyMapper = get(), temperaturePrefs = get()) }
    factory { GeocodingMapper() }
}