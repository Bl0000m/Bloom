package kz.bloom.ui.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
public fun LabeledTextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    shape: Shape = RoundedCornerShape(12.dp),
    labelStyle: TextStyle = LocalTextStyle.current.copy(color = Color(color = 0xFF555555)),
    textFieldStyle: TextStyle = labelStyle,
    placeholder: String?,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    labelColor: Color = Color(0xFF555555),
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    additionalContent: @Composable (() -> Unit)? = null,
    additionalContentColor: Color = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        Color(color = 0xFF757575)
    },
    textColor: Color = if (isError) {
        MaterialTheme.colorScheme.onError
    } else {
        MaterialTheme.colorScheme.onSurface
    },
    backgroundColor: Color = Color.Transparent,
    placeholderColor: Color = MaterialTheme.colorScheme.secondary,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CustomTextField(
            modifier = Modifier
                .clip(shape = shape)
                .then(other = textFieldModifier),
            value = value,
            textStyle = textFieldStyle,
            onValueChange = onValueChange,
            placeholder = placeholder,
            enabled = enabled,
            isError = isError,
            maxLines = maxLines,
            readOnly = readOnly,
            singleLine = singleLine,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            textColor = textColor,
            backgroundColor = backgroundColor,
            placeholderColor = placeholderColor,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
        )
        additionalContent?.let {
            CompositionLocalProvider(LocalContentColor provides additionalContentColor) {
                it()
            }
        }
    }
}