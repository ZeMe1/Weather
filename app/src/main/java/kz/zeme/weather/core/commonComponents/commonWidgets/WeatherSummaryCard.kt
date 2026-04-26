package kz.zeme.weather.core.commonComponents.commonWidgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.zeme.weather.domain.model.WeatherSummary
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme
import kz.zeme.weather.shared.resources.constants.SymbolConstants

@Composable
fun WeatherSummaryCard(
    weatherSummary: WeatherSummary,
    modifier: Modifier = Modifier,
    isMain: Boolean = false,
) {
    val maxText = "${stringResource(R.string.max)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}${weatherSummary.maxWeatherTemp}${SymbolConstants.DEGREE}"
    val minText = "${stringResource(R.string.min)}${SymbolConstants.DOT}${SymbolConstants.DOUBLE_DOT}${weatherSummary.minWeatherTemp}${SymbolConstants.DEGREE}"


    Card(
        modifier = modifier.fillMaxWidth(),
        shape = WeatherTheme.shapes.card
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            AsyncImage(
                model = weatherSummary.backgroundRes,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = WeatherTheme.dimensions.smallMediumPadding,
                        horizontal = WeatherTheme.dimensions.mediumPadding
                    ),
                verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.smallPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = weatherSummary.cityName,
                            style = WeatherTheme.typography.weight500Size20LineHeight20,
                            color = WeatherTheme.colors.textWhite
                        )
                        Spacer(Modifier.height(WeatherTheme.dimensions.extraSmallPadding))

                        if (isMain) {
                            CurrentLocationLabel()
                        } else {
                            LastFetchedTimeLabel(time = weatherSummary.lastFetchedTime)
                        }
                    }
                    Text(
                        text = "${weatherSummary.weatherTemp}${SymbolConstants.DEGREE}",
                        style = WeatherTheme.typography.weight300Size48LineHeight48,
                        color = WeatherTheme.colors.textWhite,
                        letterSpacing = 4.sp
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = weatherSummary.weatherDescription,
                        style = WeatherTheme.typography.weight500Size12LineHeight12,
                        color = WeatherTheme.colors.textCyan
                    )
                    Text(
                        text = "$maxText${SymbolConstants.COMMA} $minText",
                        style = WeatherTheme.typography.weight700Size10LineHeight10,
                        color = WeatherTheme.colors.textWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentLocationLabel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.current_location),
            style = WeatherTheme.typography.weight500Size12LineHeight12,
            color = WeatherTheme.colors.textCyan
        )
        Spacer(Modifier.width(WeatherTheme.dimensions.extraSmallPadding))
        Text(
            text = SymbolConstants.CENTER_DOT,
            style = WeatherTheme.typography.weight500Size12LineHeight12,
            color = WeatherTheme.colors.textCyan
        )
        Spacer(Modifier.width(WeatherTheme.dimensions.extraSmallPadding))
        Icon(
            painter = painterResource(R.drawable.ic_case),
            contentDescription = null,
            tint = WeatherTheme.colors.textCyan,
            modifier = Modifier.size(WeatherTheme.dimensions.smallMediumPadding)
        )
        Spacer(Modifier.width(WeatherTheme.dimensions.extraSmallPadding))
        Text(
            text = stringResource(R.string.hardcode_work),
            style = WeatherTheme.typography.weight500Size12LineHeight12,
            color = WeatherTheme.colors.textCyan
        )
    }
}

@Composable
private fun LastFetchedTimeLabel(
    time: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = time,
        style = WeatherTheme.typography.weight500Size12LineHeight12,
        color = WeatherTheme.colors.textCyan,
        modifier = modifier
    )
}

@Composable
private fun CardBackground(
    @DrawableRes backgroundRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(backgroundRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}