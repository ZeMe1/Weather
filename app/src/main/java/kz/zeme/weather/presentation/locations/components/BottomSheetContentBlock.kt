package kz.zeme.weather.presentation.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun BottomSheetContentBlock(
    firstRowIcon: Int,
    secondRowIcon: Int,
    firstRowText: Int,
    secondRowText: Int,
    onFirstRowClick: () -> Unit,
    onSecondRowClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.mediumLargePadding)
    ) {
        BottomSheetContentRow(
            icon = firstRowIcon,
            text = firstRowText,
            onClick = onFirstRowClick
        )
        BottomSheetContentRow(
            icon = secondRowIcon,
            text = secondRowText,
            onClick = onSecondRowClick
        )
    }
}

@Composable
fun BottomSheetContentRow(
    icon: Int,
    text: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = WeatherTheme.colors.white
        )
        Spacer(Modifier.width(WeatherTheme.dimensions.smallMediumPadding))
        Text(
            text = stringResource(text),
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
    }
}
