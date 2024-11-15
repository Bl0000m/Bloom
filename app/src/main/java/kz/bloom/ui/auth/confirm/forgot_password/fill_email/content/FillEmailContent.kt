package kz.bloom.ui.auth.confirm.forgot_password.fill_email.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent.Event
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton
import kz.bloom.ui.ui_components.snackbar.Snackbar
import kz.bloom.ui.ui_components.snackbar.SnackbarController

@Composable
fun FillEmailContent(modifier: Modifier = Modifier, component: FillEmailComponent) {
    val model by component.model.subscribeAsState()

    val focusRequester = remember { FocusRequester() }

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
            verticalArrangement = Arrangement.spacedBy(21.dp)
        ) {
            Icon(
                modifier = Modifier.clickable { component.onNavigateBack() },
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = "ЗАБЫЛИ ПАРОЛЬ"

                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFAAAAAA),
                    text = "Пожалуйста, введите ваш электронный адрес для сброса пароля"
                )
            }
            LabeledTextField(
                modifier = Modifier.focusRequester(focusRequester),
                singleLine = true,
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = {
                    component.fillEmail(email = it)
                },
                label = "ЭЛЕКТРОННАЯ ПОЧТА",
                placeholder = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                value = model.email,
                keyboardActions = KeyboardActions(onDone = { focusRequester.freeFocus() })
            )

            PrimaryButton(
                modifier = Modifier.padding(top = 39.dp),
                text = "ПРОДОЛЖИТЬ",
                onClick = { component.continueAndGetCode() }
            )
        }
        Snackbar(
            modifier = Modifier.align(Alignment.TopCenter),
            hostState = snackbarHostState
        )
    }
}
