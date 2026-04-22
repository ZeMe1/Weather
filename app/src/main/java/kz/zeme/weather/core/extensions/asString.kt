package kz.zeme.weather.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    @Immutable
    data class DynamicString(val value: String) : UiText
    @Immutable
    data class Resource(val res: Int) : UiText
}

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.Resource -> stringResource(res)
    }
}