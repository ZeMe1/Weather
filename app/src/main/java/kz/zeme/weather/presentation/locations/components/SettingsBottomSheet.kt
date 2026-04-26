package kz.zeme.weather.presentation.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kz.zeme.weather.core.commonComponents.bottomSheet.BaseBottomSheet
import kz.zeme.weather.core.commonComponents.commonWidgets.CommonHorizontalDivider
import kz.zeme.weather.presentation.locations.LocationsIntent
import kz.zeme.weather.shared.resources.R
import kz.zeme.weather.shared.resources.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    isVisible: Boolean,
    onIntent: (LocationsIntent) -> Unit
) {
    BaseBottomSheet(
        isVisible = isVisible,
        containerColor = WeatherTheme.colors.bottomSheetBackground.copy(0.95f),
        onDismissRequest = { onIntent(LocationsIntent.HideBottomSheet) }
    ) {
        SettingsBottomSheetContent(
            onIntent = onIntent
        )
    }
}

@Composable
private fun SettingsBottomSheetContent(
    onIntent: (LocationsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WeatherTheme.dimensions.mediumPadding)
            .padding(bottom = WeatherTheme.dimensions.mediumPadding),
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.mediumPadding)
    ) {
        BottomSheetContentBlock(
            firstRowIcon = R.drawable.ic_edit,
            secondRowIcon = R.drawable.ic_bell,
            firstRowText = R.string.change_list,
            secondRowText = R.string.notification,
            onFirstRowClick = {  },
            onSecondRowClick = {  }
        )
        CommonHorizontalDivider()
        BottomSheetContentBlock(
            firstRowIcon = R.drawable.ic_celsius,
            secondRowIcon = R.drawable.ic_fahrenheit,
            firstRowText = R.string.celsius,
            secondRowText = R.string.fahrenheit,
            onFirstRowClick = {  },
            onSecondRowClick = {  }
        )
        CommonHorizontalDivider()
        BottomSheetContentRow(
            icon = R.drawable.ic_statistics,
            text = R.string.units,
            onClick = {  }
        )
        CommonHorizontalDivider()
        BottomSheetContentRow(
            icon = R.drawable.ic_message,
            text = R.string.warn_about_problem,
            onClick = {  }
        )
    }
}