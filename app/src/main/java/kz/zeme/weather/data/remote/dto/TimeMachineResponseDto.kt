package kz.zeme.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TimeMachineResponseDto(
    @SerializedName("data") val data: List<TimeMachineDataDto>
)
