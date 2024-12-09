package kz.bloom.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    isAlternative: Boolean = false,
    isEnabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    val borderColor = if (isEnabled) Color.Black else Color(0x80CCCCCC)
    val textColor = if (isEnabled) {
        if (isAlternative) Color.White else Color.Black
    } else {
        Color(0x80CCCCCC)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isAlternative) Color.Black else Color.White,
                shape = MaterialTheme.shapes.small
            )
            .height(height = if (isAlternative) 40.dp else 32.dp)
            .border(width = 0.5.dp, color = borderColor)
            .then(
                if (isEnabled) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}