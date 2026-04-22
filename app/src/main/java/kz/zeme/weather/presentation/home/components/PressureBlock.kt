package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun PressureBlock(
    pressure: Int,
    modifier: Modifier = Modifier
) {
    CommonWeatherDetailCard(
        headerText = stringResource(R.string.pressure),
        headerIcon = R.drawable.ic_pressure,
        modifier = modifier
    ) {
        Text(
            text = pressure.toString(),
            style = WeatherTheme.typography.weight500Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )

        Spacer(Modifier.height(WeatherTheme.dimensions.extraLargePadding).weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.extraSmallPadding)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.hpa),
                style = WeatherTheme.typography.weight400Size15LineHeight18,
                color = WeatherTheme.colors.textWhite
            )
        }
    }
}