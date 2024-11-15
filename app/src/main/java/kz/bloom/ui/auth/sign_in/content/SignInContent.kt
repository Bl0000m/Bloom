package kz.bloom.ui.auth.sign_in.content

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton
import kz.bloom.ui.ui_components.snackbar.SnackbarController
import kz.bloom.ui.ui_components.snackbar.Snackbar
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Event

@Composable
fun SignInContent(modifier: Modifier, component: SignInComponent) {

    val model by component.model.subscribeAsState()

    val focusManager = LocalFocusManager.current

    val emailFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester() }

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
            when(event) {
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
            verticalArrangement = Arrangement.spacedBy(44.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(33.dp)
            ) {
                Icon(
                    modifier = Modifier.clickable { component.navigateBack() },
                    painter = painterResource(id = R.drawable.ic_arrow_back_black),
                    contentDescription = null
                )

                Text(
                    text = "ВОЙТИ В АККАУНТ",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LabeledTextField(
                    modifier = Modifier.focusRequester(emailFocusRequest),
                    label = "ЭЛЕКТРОННАЯ ПОЧТА",
                    placeholder = "",
                    singleLine = true,
                    labelStyle = MaterialTheme.typography.labelSmall,
                    onValueChange = { component.fillEmail(email = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequest.requestFocus() }
                    ),
                    value = model.email
                )
                LabeledTextField(
                    modifier = Modifier.focusRequester(passwordFocusRequest),
                    label = "ПАРОЛЬ",
                    singleLine = true,
                    placeholder = "",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    onValueChange = { component.fillPassword(password = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    value = model.password,
                )
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PrimaryButton(text = "ВОЙТИ В АККАУНТ", onClick = { component.enterAccount() })
                    Text(
                        modifier = Modifier.clickable { component.forgotPassword() },
                        text = "Забыли пароль?", style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Column(
                modifier = Modifier.padding(top = 74.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Text(text = "НЕТ АККАУНТА?", style = MaterialTheme.typography.labelLarge)
                PrimaryButton(text = "СОЗДАТЬ АККАУНТ", onClick = { component.createAccount() })
            }
        }
        Snackbar(
            modifier = Modifier.align(Alignment.TopCenter),
            hostState = snackbarHostState
        )
    }
}