package kz.bloom.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String?,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    additionalContent: @Composable (() -> Unit)? = null,
    additionalContentColor: Color = Color(0xFF757575),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = Color.Transparent,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Column (verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(Modifier.padding(top = 6.dp)) {
            val colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = cursorColor,
                errorCursorColor = cursorColor,
                focusedPlaceholderColor = placeholderColor,
                unfocusedPlaceholderColor = placeholderColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )

            BasicTextField(
                enabled = enabled,
                modifier = modifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle.merge(TextStyle(color = textColor)),
                cursorBrush = SolidColor(cursorColor),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        placeholder = {
                            if (placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = textStyle.copy(color = placeholderColor)
                                )
                            }
                        },
                        leadingIcon = leadingContent,
                        trailingIcon = trailingContent,
                        singleLine = singleLine,
                        enabled = enabled,
                        colors = colors,
                        contentPadding = PaddingValues(
                            vertical = 4.dp
                        ),
                        interactionSource = remember { MutableInteractionSource() },
                        visualTransformation = visualTransformation
                    )
                },
                maxLines = maxLines,
                readOnly = readOnly,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation
            )
        }

        additionalContent?.let {
            CompositionLocalProvider(LocalContentColor provides additionalContentColor) {
                it()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier,
                thickness = 1.dp,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
            )
        }

    }
}