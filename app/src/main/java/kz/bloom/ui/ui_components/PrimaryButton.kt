package kz.bloom.ui.ui_components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
public fun PrimaryButton(modifier: Modifier = Modifier, textStyle: TextStyle, text: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 32.dp)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = textStyle
        )
    }
}