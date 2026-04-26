package kz.zeme.weather.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlin.reflect.KClass

inline fun <reified T : Any> NavController.popBackStackWithResult(resultKey: String, resultValue: T) {
    previousBackStackEntry?.savedStateHandle?.set(resultKey, resultValue)
    popBackStack()
}

inline fun <reified T : Any, R : Any> NavController.popBackStackWithResult(
    resultKey: String,
    resultValue: T,
    route: R,
    inclusive: Boolean,
) {
    if (inclusive) {
        val isPopped = popBackStack(route, true)
        if (isPopped) currentBackStackEntry?.savedStateHandle?.set(resultKey, resultValue)
    } else {
        getBackStackEntry(route).savedStateHandle[resultKey] = resultValue
        popBackStack(route, false)
    }
}

@Composable
inline fun <reified T : Any> LaunchedEffectNavigationResult(
    navController: NavController,
    resultKey: String,
    noinline onResult: suspend (T) -> Unit,
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return
    val resultFlow = remember(savedStateHandle, resultKey) {
        savedStateHandle.getStateFlow<T?>(resultKey, null)
    }
    val result by resultFlow.collectAsState()

    LaunchedEffect(result) {
        result?.let {
            savedStateHandle.remove<T>(resultKey)
            onResult.invoke(it)
        }
    }
}

@Composable
fun LaunchedEffectNavigationCallback(
    navController: NavController,
    resultKey: String,
    callback: suspend () -> Unit,
) {
    LaunchedEffectNavigationResult<Boolean>(
        navController = navController,
        resultKey = resultKey,
        onResult = { result -> if (result) callback.invoke() }
    )
}


fun NavController.getCurrentGraphEntry(graphRoute: Destination): NavBackStackEntry? {
    return runCatching { getBackStackEntry(graphRoute) }.getOrElse { getBackStackEntry(graph.startDestinationRoute ?: return null) }
}

fun NavController.getCurrentGraphEntry(graphRouteClass: KClass<out Destination>): NavBackStackEntry? {
    return runCatching { getBackStackEntry(graphRouteClass) }.getOrElse { getBackStackEntry(graph.startDestinationRoute ?: return null) }
}