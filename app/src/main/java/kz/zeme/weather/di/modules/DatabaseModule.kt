package kz.zeme.weather.di.modules

import androidx.room.Room
import kz.zeme.weather.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "weather_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    single {
        get<AppDatabase>().weatherDao()
    }
}