package kz.zeme.weather.presentation.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.domain.model.City
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun SearchSuggestionResultRow(
    city: City,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtitle = listOfNotNull(city.state, city.country).joinToString(", ")

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .safeClickable { onClick() }
                .padding(
                    horizontal = WeatherTheme.dimensions.mediumPadding,
                    vertical = WeatherTheme.dimensions.mediumPadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.extraSmallPadding)
            ) {
                Text(
                    text = city.name,
                    style = WeatherTheme.typography.weight500Size18LineHeight21,
                    color = WeatherTheme.colors.textWhite
                )
                Text(
                    text = subtitle,
                    style = WeatherTheme.typography.weight400Size14LineHeight20,
                    color = WeatherTheme.colors.textWhite.copy(0.6f)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = WeatherTheme.colors.textWhite.copy(0.6f)
            )
        }
        HorizontalDivider(
            color = WeatherTheme.colors.textWhite.copy(alpha = 0.1f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = WeatherTheme.dimensions.mediumPadding)
        )
    }
}