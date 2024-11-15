package kz.bloom.ui.auth.pass_code.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent.Event
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent
import kz.bloom.ui.ui_components.snackbar.Snackbar
import kz.bloom.ui.ui_components.snackbar.SnackbarController

@Composable
fun PassContent(modifier: Modifier = Modifier, component: PassCodeComponent) {
    val model by component.model.subscribeAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarController = remember {
        SnackbarController(
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }

    LaunchedEffect(Unit) {
        component.events.collect { event ->
            when (event) {
                is Event.DisplaySnackBar -> {
                    snackbarController.showSnackbar(message = event.errorMessage)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 42.dp)
                .padding(horizontal = 21.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { component.onBackClick() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_arrow_back_black),
                        contentDescription = null
                    )
                }
                IconButton(onClick = { component.onCloseClick() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_close_square_light),
                        contentDescription = null
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ИСПОЛЬЗОВАТЬ КОД-ПАРОЛЬ",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                if (model.pinLength == 4) {
                    Text(
                        text = "Введите ПИН-код еще раз для подтверждения",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = if (model.pinCodeMissMatch) {
                        "ПИН-коды не совпадают"
                    } else "Создайте ПИН-код для быстрого доступа к приложению",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = if (model.pinCodeMissMatch) MaterialTheme.colorScheme.error else Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(49.dp, 2.dp)
                            .background(
                                if (index < model.pinLength.coerceAtMost(4)) {
                                    Color.Black
                                } else if (model.pinCodeMissMatch) {
                                    MaterialTheme.colorScheme.error
                                } else Color.Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            if (model.pinLength >= 4) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(49.dp, 2.dp)
                                .background(
                                    if (index < (model.pinLength - 4).coerceAtLeast(0)) {
                                        Color.Black
                                    } else if (model.pinCodeMissMatch) {
                                        MaterialTheme.colorScheme.error
                                    } else Color.Gray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                val numbers = listOf(
                    listOf(1, 2, 3),
                    listOf(4, 5, 6),
                    listOf(7, 8, 9),
                    listOf(null, 0, -1)
                )

                numbers.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(29.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { number ->
                            when (number) {
                                null -> Spacer(modifier = Modifier.size(69.dp))
                                -1 -> Button(
                                    modifier = Modifier
                                        .size(69.dp)
                                        .border(
                                            BorderStroke(1.dp, Color.Black),
                                            CircleShape.copy(all = CornerSize(size = 90.dp))
                                        ),
                                    onClick = { component.onDeleteClick() },
                                    shape = CircleShape.copy(all = CornerSize(size = 90.dp))
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_back_space),
                                        modifier = Modifier.size(32.dp),
                                        contentDescription = null
                                    )
                                }

                                else -> NumberButton(number) { component.onNumberClick(number) }
                            }
                        }
                    }
                }
            }
        }
        Snackbar(
            modifier = Modifier.align(Alignment.TopCenter),
            hostState = snackbarHostState
        )
    }
}

@Composable
fun NumberButton(number: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape.copy(all = CornerSize(size = 90.dp)),
        modifier = Modifier
            .size(69.dp)
            .border(
                BorderStroke(1.dp, Color.Black),
                CircleShape.copy(all = CornerSize(size = 90.dp))
            ),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 24.sp)
        )
    }
}