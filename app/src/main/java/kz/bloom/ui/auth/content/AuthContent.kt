package kz.bloom.ui.auth.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.component.AuthComponent
import kz.bloom.R
import kz.bloom.ui.ui_components.CustomTextField

@Composable
fun AuthContent(component: AuthComponent) {

    val model by component.model.subscribeAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 21.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 51.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(33.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )

            Text(
                text = "СОЗДАТЬ АККАУНТ",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomTextField(
                value = model.name,
                placeholder = "ИМЯ",
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillName(name = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            CustomTextField(
                value = model.email,
                singleLine = true,
                placeholder = "ЭЛЕКТРОННАЯ ПОЧТА",
                textStyle = MaterialTheme.typography.labelSmall,
                onValueChange = {component.fillMail(email = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )


            CustomTextField(
                value = model.password,
                singleLine = true,
                placeholder = "ПАРОЛЬ",
                textStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPassword(password = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            CustomTextField(
                value = model.passwordConfirm,
                singleLine = true,
                placeholder = "ПОВТОРИТЕ ПАРОЛЬ",
                textStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPasswordConfirm(rePassword = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }
    }
}