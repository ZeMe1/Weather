package kz.zeme.weather.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.zeme.weather.domain.useCase.GetWeatherUseCase

class MainViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
): ViewModel() {

    fun loadWeather() {
        viewModelScope.launch {
            val result = getWeatherUseCase()

            result.onSuccess { weatherData ->
                Log.d("ZeMe", "SUCCESS: $weatherData")
            }.onFailure { error ->
                Log.e("ZeMe", "ERROR: ${error.message}", error)
            }
        }
    }
}