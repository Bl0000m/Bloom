package kz.bloom.ui.auth.pass_code.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent

@Composable
fun PassContent(modifier: Modifier = Modifier, component: PassCodeComponent) {
    val model by component.model.subscribeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back and close buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { component.onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
            IconButton(onClick = { component.onCloseClick() }) {
                Icon(Icons.Default.Close, contentDescription = "Закрыть")
            }
        }

        // Title and Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ИСПОЛЬЗОВАТЬ КОД-ПАРОЛЬ",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Создайте ПИН-код для быстрого доступа к приложению",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }

        // PIN Input Indicators
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp, 2.dp)
                        .background(
                            if (index < model.pinLength) Color.Black else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        // Numeric Keypad
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val numbers = listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9),
                listOf(null, 0, -1)
            )

            numbers.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { number ->
                        when (number) {
                            null -> Spacer(modifier = Modifier.size(64.dp))
                            -1 -> IconButton(onClick = { component.onDeleteClick() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Удалить")
                            }
                            else -> NumberButton(number) { component.onNumberClick(number) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(number: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier.size(69.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}