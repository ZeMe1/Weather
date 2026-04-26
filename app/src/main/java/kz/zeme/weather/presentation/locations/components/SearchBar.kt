package kz.zeme.weather.presentation.locations.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kz.zeme.weather.core.commonComponents.textField.PrimaryTextField
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun SearchBar(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
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
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = WeatherTheme.dimensions.largePadding)
            .padding(bottom = WeatherTheme.dimensions.mediumPadding)
            .imePadding()
    )
}