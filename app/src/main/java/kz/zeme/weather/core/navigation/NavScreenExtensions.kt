package kz.zeme.weather.core.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kz.zeme.weather.core.navigation.TypeMapProvider.Companion.getTypeMap

inline fun <reified T : Any> NavGraphBuilder.screen(
    typeMapProvider: TypeMapProvider? = null,
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    noinline popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    noinline popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? = null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry, T) -> Unit
) {
    val typeMap = getTypeMap(typeMapProvider)
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = enterTransition ?: {
            val route = runCatching { targetState.toRoute<T>() }.getOrNull()
            (route as? TransitionAnimation)?.enterTransition?.invoke(this)
        },
        exitTransition = exitTransition ?: {
            val route = runCatching { initialState.toRoute<T>() }.getOrNull()
            (route as? TransitionAnimation)?.exitTransition?.invoke(this)
        },
        popEnterTransition = popEnterTransition ?: {
            val route = runCatching { targetState.toRoute<T>() }.getOrNull()
            (route as? TransitionAnimation)?.popEnterTransition?.invoke(this)
        },
        popExitTransition = popExitTransition ?: {
            val route = runCatching { initialState.toRoute<T>() }.getOrNull()
            (route as? TransitionAnimation)?.popExitTransition?.invoke(this)
        },
        sizeTransform = sizeTransform,
        content = { entry -> content.invoke(this, entry, entry.toRoute<T>()) }
    )
}

inline fun <reified T : Any> NavGraphBuilder.dialogScreen(
    typeMapProvider: TypeMapProvider? = null,
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable (NavBackStackEntry, T) -> Unit
) {
    val typeMap = getTypeMap(typeMapProvider)
    dialog<T>(
        typeMap = typeMap,
        dialogProperties = dialogProperties,
        content = { entry ->
            content.invoke(entry, entry.toRoute<T>())
        }
    )
}