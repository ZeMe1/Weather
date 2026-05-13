package kz.zeme.weather.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class City(
    val name: String,
    val state: String?,
    val country: String,
    val latitude: Double,
    val longitude: Double,
)