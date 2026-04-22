package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun WindInformationRow(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leftText,
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
        Text(
            text = rightText,
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite.copy(0.5f)
        )
    }
}