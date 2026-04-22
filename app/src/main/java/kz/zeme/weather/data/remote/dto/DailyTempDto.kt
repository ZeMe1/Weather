package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DailyTempDto(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    @SerializedName("morn") val morning: Double
)