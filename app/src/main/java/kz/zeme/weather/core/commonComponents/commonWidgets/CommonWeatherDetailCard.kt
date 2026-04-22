package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun CommonWeatherDetailCard(
    headerText: String,
    headerIcon: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = WeatherTheme.colors.cardCyan.copy(0.5f)),
        shape = WeatherTheme.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = WeatherTheme.dimensions.mediumPadding, vertical = WeatherTheme.dimensions.smallMediumPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.extraSmallPadding)
            ) {
                Icon(
                    painter = painterResource(headerIcon),
                    contentDescription = null,
                    tint = WeatherTheme.colors.white.copy(0.5f)
                )
                Text(
                    text = headerText.uppercase(),
                    style = WeatherTheme.typography.weight500Size12LineHeight12,
                    color = WeatherTheme.colors.white.copy(0.5f)
                )
            }
            Spacer(Modifier.height(WeatherTheme.dimensions.smallPadding))

            content()
        }
    }
}