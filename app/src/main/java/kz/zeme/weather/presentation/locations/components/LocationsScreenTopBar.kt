package kz.zeme.weather.presentation.locations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun LocationsScreenTopBar(
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.weather),
            style = WeatherTheme.typography.weight700Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )
        Box(
            modifier = Modifier
                .clip(WeatherTheme.shapes.surface)
                .border(
                    width = 1.dp,
                    shape = WeatherTheme.shapes.surface,
                    color = WeatherTheme.colors.white.copy(0.5f)
                )
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        WeatherTheme.colors.textFieldBackground.copy(0f),
                        WeatherTheme.colors.textFieldBackground.copy(0.5f)
                    )
                )),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more_dots),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(WeatherTheme.dimensions.smallPadding)
                    .safeClickable { onMoreClick() }
            )
        }
    }
}