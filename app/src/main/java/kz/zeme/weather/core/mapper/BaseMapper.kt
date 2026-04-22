package kz.zeme.weather.core.mapper

fun interface BaseMapper<FROM, TO> {
    fun map(source: FROM): TO
}