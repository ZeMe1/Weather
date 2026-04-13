package kz.zeme.weather.shared.core.mapper

fun interface BaseMapper<FROM, TO> {
    fun map(source: FROM): TO
}