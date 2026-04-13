package kz.zeme.weather.shared.core.mapper

interface BiMapper<E, D> : BaseMapper<E, D> {
    fun reverse(source: D): E
}