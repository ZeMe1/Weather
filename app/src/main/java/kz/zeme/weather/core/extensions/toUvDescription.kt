package kz.zeme.weather.core.extensions

import kz.zeme.weather.shared.resources.R

fun Int.toUvDescription(): Int = when(this) {
    in 0..2 -> R.string.uv_desc_low
    in 3..5 -> R.string.uv_desc_mid
    in 6..7 -> R.string.uv_desc_high
    in 8..10 -> R.string.uv_desc_very_high
    else -> R.string.uv_desc_extreme
}
// Need to change the logic here