package kz.zeme.weather.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

sealed interface TransitionAnimation {
    val enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) get() = { null }
    val exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) get() = { null }
    val popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) get() = enterTransition
    val popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) get() = exitTransition

    interface SlideFade : TransitionAnimation {
        override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
            get() = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() }

        override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
            get() = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() }

        override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
            get() = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() }

        override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
            get() = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    }

    interface Fade : TransitionAnimation {
        override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? get() = { fadeOut() }
        override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? get() = { fadeIn() }
    }

    interface None : TransitionAnimation {
        override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? get() = { null }
        override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? get() = { null }
    }
}