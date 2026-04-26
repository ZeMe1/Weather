package kz.zeme.weather.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kz.zeme.weather.core.navigation.destination.WeatherDestinations
import kz.zeme.weather.core.navigation.destination.weatherGraph

@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WeatherDestinations.Graph
    ) {
        weatherGraph(navController)
    }
}