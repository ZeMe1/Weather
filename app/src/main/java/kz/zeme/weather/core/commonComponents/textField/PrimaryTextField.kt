package kz.zeme.weather.core.commonComponents.textField

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kz.zeme.weather.shared.resources.WeatherTheme

@Composable
fun PrimaryTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    placeholderRes: Int? = null,
    labelRes: Int? = null,
    label: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    textStyle: TextStyle = WeatherTheme.typography.weight400Size15LineHeight18,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = primaryTextFieldColors(),
    onClick: (() -> Unit)? = null,
    filter: FieldFilter = object : FieldFilter {},
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val controller = rememberStringController(value, onValueChange, filter)
    PrimaryTextFieldInternal(
        controller = controller,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        isError = isError,
        maxLength = maxLength,
        errorMessage = errorMessage,
        placeholderRes = placeholderRes,
        labelRes = labelRes,
        label = label,
        maxLines = maxLines,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        colors = colors,
        onClick = onClick,
        singleLine = singleLine,
        enabled = enabled,
        interactionSource = interactionSource
    )
}

@Composable
private fun PrimaryTextFieldInternal(
    controller: TextFieldController,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    placeholderRes: Int? = null,
    labelRes: Int? = null,
    label: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    singleLine: Boolean,
    textStyle: TextStyle = WeatherTheme.typography.weight400Size15LineHeight18,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = primaryTextFieldColors(),
    onClick: (() -> Unit)? = null,
    enabled: Boolean = false,
    interactionSource: MutableInteractionSource
) {
    val contentColor = if (readOnly) colors.disabledTextColor else WeatherTheme.colors.textWhite
    var hasFocusedOnce by remember { mutableStateOf(false) }

    LaunchedEffect(onClick) {
        if (onClick != null) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Release -> onClick.invoke()
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        when {
            label != null -> label()
            labelRes != null -> Text(
                text = stringResource(labelRes),
                modifier = Modifier.padding(bottom = 8.dp),
                style = WeatherTheme.typography.weight400Size15LineHeight18
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && !hasFocusedOnce) {
                        hasFocusedOnce = true
                        controller.onValueChange(
                            controller.value.copy(selection = TextRange(controller.value.text.length))
                        )
                    }
                },
            value = controller.value,
            onValueChange = { controller.onValueChange(it.copy(text = it.text.take(maxLength))) },
            textStyle = textStyle.copy(color = contentColor, lineHeight = 1.em),
            isError = isError,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            maxLines = maxLines,
            shape = RoundedCornerShape(16.dp),
            colors = colors,
            interactionSource = interactionSource,
            placeholder = {
                placeholderRes?.let { res ->
                    Text(
                        text = stringResource(res),
                        style = WeatherTheme.typography.weight400Size15LineHeight18,
                        color = WeatherTheme.colors.textWhite.copy(0.5f)
                    )
                }
            },
            singleLine = singleLine,
            enabled = enabled
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = WeatherTheme.typography.weight400Size15LineHeight18,
                modifier = Modifier.padding(top = WeatherTheme.dimensions.smallPadding)
            )
        }
    }
}

@Composable
private fun primaryTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = WeatherTheme.colors.textFieldBackground.copy(0.7f),
        unfocusedContainerColor = WeatherTheme.colors.textFieldBackground.copy(0.7f),
        disabledContainerColor = WeatherTheme.colors.textFieldBackground.copy(0.7f),
        cursorColor = WeatherTheme.colors.textWhite,
        focusedTextColor = WeatherTheme.colors.textWhite,
        unfocusedTextColor = WeatherTheme.colors.textWhite,
        focusedBorderColor = WeatherTheme.colors.white,
        unfocusedBorderColor = WeatherTheme.colors.white.copy(0.5f),
        focusedPlaceholderColor = WeatherTheme.colors.textWhite.copy(0.5f),
        unfocusedPlaceholderColor = WeatherTheme.colors.textWhite.copy(0.5f)
    ).copy(
        errorIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
}