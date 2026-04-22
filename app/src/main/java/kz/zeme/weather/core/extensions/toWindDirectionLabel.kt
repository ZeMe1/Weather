package kz.zeme.weather.core.extensions

import kz.zeme.weather.shared.resources.R

fun Int.toWindDirectionLabel(): Int {
    val directions = listOf(
        R.string.wind_direction_n,
        R.string.wind_direction_ne,
        R.string.wind_direction_e,
        R.string.wind_direction_se,
        R.string.wind_direction_s,
        R.string.wind_direction_sw,
        R.string.wind_direction_w,
        R.string.wind_direction_nw
    )
    val index = ((this + ADDITION_VALUE) / DIVISION_VALUE).toInt() % 8
    return directions[index]
}

const val ADDITION_VALUE = 22.5
const val DIVISION_VALUE = 45
