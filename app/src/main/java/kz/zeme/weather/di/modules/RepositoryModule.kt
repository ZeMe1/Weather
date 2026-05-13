package kz.zeme.weather.di.modules

import com.google.android.gms.location.LocationServices
import kz.zeme.weather.data.repository.GeocodingRepositoryImpl
import kz.zeme.weather.data.repository.WeatherRepositoryImpl
import kz.zeme.weather.data.service.location.DefaultLocationProvider
import kz.zeme.weather.data.service.network.DefaultNetworkChecker
import kz.zeme.weather.domain.repository.GeocodingRepository
import kz.zeme.weather.domain.repository.WeatherRepository
import kz.zeme.weather.domain.service.LocationService
import kz.zeme.weather.domain.service.NetworkService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
    single<LocationService> { DefaultLocationProvider(fusedLocationProviderClient = get(), application = androidApplication()) }
    single<NetworkService> { DefaultNetworkChecker(context = get()) }

    single<WeatherRepository> { WeatherRepositoryImpl(api = get(), dao = get(), locationService = get(), networkService = get(), weatherMapper = get(), hourlyMapper = get(), dailyMapper = get(), historyWeatherMapper = get(), weatherMapperLocal = get()) }
    single<GeocodingRepository> { GeocodingRepositoryImpl(api = get(), networkService = get(), mapper = get()) }
}