package kz.zeme.weather.di

import android.app.Application
import kz.zeme.weather.di.modules.mapperModule
import kz.zeme.weather.di.modules.networkModule
import kz.zeme.weather.di.modules.repositoryModule
import kz.zeme.weather.di.modules.useCaseModule
import kz.zeme.weather.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WeatherApp)
            modules(
                networkModule,
                useCaseModule,
                viewModelModule,
                repositoryModule,
                mapperModule
            )
        }
    }
}