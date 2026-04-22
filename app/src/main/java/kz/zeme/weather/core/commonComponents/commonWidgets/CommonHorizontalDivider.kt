package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun CommonHorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = WeatherTheme.colors.white.copy(0.1f)
    )
}