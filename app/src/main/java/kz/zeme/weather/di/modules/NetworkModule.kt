package kz.zeme.weather.di.modules

import kz.zeme.weather.BuildConfig
import kz.zeme.weather.core.preference.TemperatureUnit
import kz.zeme.weather.core.preference.TemperatureUnitPreferences
import kz.zeme.weather.data.remote.api.GeocodingApi
import kz.zeme.weather.data.remote.api.WeatherApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val temperaturePrefs: TemperatureUnitPreferences = get()

        val apiKeyInterceptor = Interceptor { chain ->
            val unit = temperaturePrefs.getCurrentUnit(temperaturePrefs.state.value)
            val units = when (unit) {
                TemperatureUnit.CELSIUS -> "metric"
                TemperatureUnit.FAHRENHEIT -> "imperial"
            }

            val originalRequest = chain.request()
            val url = originalRequest.url.newBuilder()
                .addQueryParameter("appid", BuildConfig.WEATHER_API_KEY)
                .addQueryParameter("units", units)
                .addQueryParameter("lang", Locale.getDefault().toLanguageTag())
                .build()

            chain.proceed(originalRequest.newBuilder().url(url).build())
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    single<GeocodingApi> { get<Retrofit>().create(GeocodingApi::class.java) }
}