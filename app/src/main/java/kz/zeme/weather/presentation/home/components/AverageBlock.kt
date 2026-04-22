package kz.zeme.weather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonWeatherDetailCard
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun AverageBlock(
    todayMaxTemp: Int,
    averageMaxTemp: Int,
    modifier: Modifier = Modifier
) {
    val difference = kotlin.math.abs(todayMaxTemp - averageMaxTemp)
    val differenceLabel = "${stringResource(R.string.on_for)} $difference${SymbolConstants.DEGREE}"
    val differenceText = if (difference > 0) R.string.more_daily_max else if (difference < 0) R.string.below_avg_max else R.string.equal_avg_max

    val todayMaxText = "${stringResource(R.string.max)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}$todayMaxTemp${SymbolConstants.DEGREE}"
    val averageMaxText = "${stringResource(R.string.max)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}$averageMaxTemp${SymbolConstants.DEGREE}"

    CommonWeatherDetailCard(
        headerText = stringResource(R.string.average),
        headerIcon = R.drawable.ic_graph,
        modifier = modifier
    ) {
        Text(
            text = differenceLabel,
            style = WeatherTheme.typography.weight500Size32LineHeight32,
            color = WeatherTheme.colors.textWhite
        )
        Text(
            text = stringResource(differenceText),
            style = WeatherTheme.typography.weight500Size15LineHeight21,
            color = WeatherTheme.colors.textWhite
        )
        Spacer(Modifier.height(WeatherTheme.dimensions.smallMediumPadding).weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.today),
                style = WeatherTheme.typography.weight500Size10LineHeight10,
                color = WeatherTheme.colors.textWhite.copy(0.5f)
            )
            Text(
                text = todayMaxText,
                style = WeatherTheme.typography.weight500Size10LineHeight10,
                color = WeatherTheme.colors.textWhite
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.average),
                style = WeatherTheme.typography.weight500Size10LineHeight10,
                color = WeatherTheme.colors.textWhite.copy(0.5f)
            )
            Text(
                text = averageMaxText,
                style = WeatherTheme.typography.weight500Size10LineHeight10,
                color = WeatherTheme.colors.textWhite
            )
        }
    }
}