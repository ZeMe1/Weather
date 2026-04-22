package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun LoadingScreen(
    isLoading: Boolean,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color = WeatherTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        content()
        AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier.fillMaxSize().background(color = WeatherTheme.colors.white.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = WeatherTheme.colors.backgroundBlue)
            }
        }
    }
}