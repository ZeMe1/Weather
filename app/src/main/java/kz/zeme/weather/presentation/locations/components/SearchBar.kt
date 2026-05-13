package kz.zeme.weather.presentation.locations.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kz.zeme.weather.core.commonComponents.textField.PrimaryTextField
import kz.zeme.weather.core.extensions.safeClickable
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusGained: () -> Unit,
    onCancel: () -> Unit,
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val cancelButtonWidth by animateDpAsState(
        targetValue = if (isSearching) 64.dp else 0.dp,
        animationSpec = tween(durationMillis = 250)
    )
    val cancelAlpha by animateFloatAsState(
        targetValue = if (isSearching) 1f else 0f,
        animationSpec = tween(durationMillis = 250)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
            .padding(bottom = WeatherTheme.dimensions.mediumPadding)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PrimaryTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholderRes = R.string.search_city_airport,
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = WeatherTheme.colors.white
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_microphone),
                    contentDescription = null,
                    tint = WeatherTheme.colors.white
                )
            },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { if (it.isFocused) onFocusGained() }
        )

        if (cancelButtonWidth > 0.dp) {
            Spacer(Modifier.width(WeatherTheme.dimensions.smallPadding))
            Box(modifier = Modifier.width(cancelButtonWidth)) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = WeatherTheme.typography.weight500Size15LineHeight21,
                    color = WeatherTheme.colors.textWhite.copy(alpha = cancelAlpha),
                    modifier = Modifier
                        .safeClickable{
                            focusManager.clearFocus()
                            onCancel()
                        }
                )
            }
        }
    }
}