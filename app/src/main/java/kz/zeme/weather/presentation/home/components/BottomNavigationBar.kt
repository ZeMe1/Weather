package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kz.zeme.weather.core.commonComponents.commonWidgets.BoxWithIcon
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun BottomNavigationBar(
    onMapCLick: () -> Unit,
    onListClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WeatherTheme.dimensions.largePadding)
            .padding(bottom = WeatherTheme.dimensions.extraLargePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BoxWithIcon(
            icon = R.drawable.ic_map,
            onClick = onMapCLick
        )
        BoxWithIcon(
            icon = R.drawable.ic_cursor
        )
        BoxWithIcon(
            icon = R.drawable.ic_list,
            onClick = onListClick
        )
    }
}
