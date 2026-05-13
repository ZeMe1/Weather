package kz.zeme.weather.data.remote.mapper

import kz.zeme.weather.core.mapper.BaseMapper
import kz.zeme.weather.data.remote.dto.GeocodingResponseDto
import kz.zeme.weather.domain.model.City

class GeocodingMapper : BaseMapper<GeocodingResponseDto, City> {
    override fun map(source: GeocodingResponseDto): City {
        return City(
            name = source.name,
            state = source.state,
            country = source.country,
            latitude = source.latitude,
            longitude = source.longitude
        )
    }
}