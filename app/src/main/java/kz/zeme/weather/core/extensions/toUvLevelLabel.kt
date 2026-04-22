package kz.zeme.weather.core.extensions

import kz.zeme.weather.shared.resources.R

fun Int.toUvLevelLabel(): Int = when (this) {
    in 0..2 -> R.string.low
    in 3..5 -> R.string.mid
    in 6..7 -> R.string.high
    in 8..10 -> R.string.very_high
    else -> R.string.extreme
}