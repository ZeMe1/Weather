package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TimeMachineDataDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("dt") val timeStamp: Long
)
