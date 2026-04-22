package kz.zeme.weather.core.mapper

interface BiMapper<E, D> : BaseMapper<E, D> {
    fun reverse(source: D): E
}