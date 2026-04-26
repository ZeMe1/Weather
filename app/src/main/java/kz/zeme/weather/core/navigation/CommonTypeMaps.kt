package kz.zeme.weather.core.navigation

import androidx.navigation.NavType
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object CommonTypeMaps {

    val Int: Map<KType, NavType<*>> = mapOf(typeOf<Int>() to NavType.IntType)
    val Long: Map<KType, NavType<*>> = mapOf(typeOf<Long>() to NavType.LongType)
    val String: Map<KType, NavType<*>> = mapOf(typeOf<String>() to NavType.StringType)
    val Boolean: Map<KType, NavType<*>> = mapOf(typeOf<Boolean>() to NavType.BoolType)
    val IntList: Map<KType, NavType<*>> = mapOf(typeOf<List<Int>>() to NavType.IntListType)
}
