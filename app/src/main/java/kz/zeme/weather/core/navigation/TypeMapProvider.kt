package kz.zeme.weather.core.navigation

import androidx.navigation.NavType
import kotlin.reflect.KType

interface TypeMapProvider {
    val typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> get() = emptyMap()

    companion object {
        fun getTypeMap(provider: TypeMapProvider?): Map<KType, NavType<*>> =
            provider?.typeMap ?: emptyMap()
    }
}