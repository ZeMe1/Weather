package kz.zeme.weather.di.modules

import com.google.android.gms.location.LocationServices
import kz.zeme.weather.data.location.DefaultLocationProvider
import kz.zeme.weather.data.repository.WeatherRepositoryImpl
import kz.zeme.weather.domain.repository.WeatherRepository
import kz.zeme.weather.domain.service.LocationService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
    single<LocationService> { DefaultLocationProvider(fusedLocationProviderClient = get(), application = androidApplication()) }

    single<WeatherRepository> { WeatherRepositoryImpl(api = get(), dao = get(), locationService = get(), weatherMapper = get(), hourlyMapper = get(), dailyMapper = get(), historyWeatherMapper = get(), weatherMapperLocal = get()) }
}