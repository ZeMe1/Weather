package kz.zeme.weather.core.commonComponents.textField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

interface FieldFilter {
    fun filter(value: String): String = value
}

interface TextFieldController {
    val value: TextFieldValue
    fun onValueChange(newValue: TextFieldValue)
}

@Composable
fun rememberStringController(
    value: String,
    onValueChange: (String) -> Unit,
    filter: FieldFilter = object : FieldFilter {},
): TextFieldController {
    val state = remember { mutableStateOf(TextFieldValue(value)) }

    LaunchedEffect(value) {
        if (state.value.text != value) {
            state.value = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    return remember {
        object : TextFieldController {
            override val value get() = state.value
            override fun onValueChange(newValue: TextFieldValue) {
                val newText = filter.filter(newValue.text)
                state.value = newValue.copy(text = newText)
                onValueChange.invoke(filter.filter(newText))
            }
        }
    }
}