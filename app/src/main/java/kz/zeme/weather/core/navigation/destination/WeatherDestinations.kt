package kz.zeme.weather.core.navigation.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import kz.zeme.weather.core.navigation.Destination
import kz.zeme.weather.core.navigation.TransitionAnimation
import kz.zeme.weather.core.navigation.screen
import kz.zeme.weather.presentation.home.HomeScreen
import kz.zeme.weather.presentation.locations.LocationsScreen

@Serializable
sealed interface WeatherDestinations : Destination, TransitionAnimation.SlideFade {
    @Serializable
    data object Graph : WeatherDestinations

    @Serializable
    data object Main : WeatherDestinations

    @Serializable
    data object LocationsScreen : WeatherDestinations
}

fun NavGraphBuilder.weatherGraph(  navController: NavHostController) {
    navigation(startDestination = WeatherDestinations.Main::class, route = WeatherDestinations.Graph::class) {
        screen<WeatherDestinations.Main> { backStackEntry, arg ->
            HomeScreen(navController = navController)
        }
        screen<WeatherDestinations.LocationsScreen> { backStackEntry, arg ->
            LocationsScreen(navController = navController)
        }
    }
}