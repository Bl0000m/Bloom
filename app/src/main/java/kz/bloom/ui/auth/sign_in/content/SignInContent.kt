package kz.bloom.ui.auth.sign_in.content

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun SignInContent(modifier: Modifier, component: SignInComponent) {

    val model by component.model.subscribeAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
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
                label = "ЭЛЕКТРОННАЯ ПОЧТА",
                placeholder = "",
                singleLine = true,
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillEmail(email = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                value = model.email
            )
            LabeledTextField(
                label = "ПАРОЛЬ",
                singleLine = true,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = {component.fillPassword(password = it) },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                value = model.password
            )
            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PrimaryButton(text = "ВОЙТИ В АККАУНТ", onClick = { component.enterAccount() } )
                Text(text = "Забыли пароль?", style = MaterialTheme.typography.labelSmall)
            }
        }

        Column(
            modifier = Modifier.padding(top = 74.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Text(text = "НЕТ АККАУНТА?", style = MaterialTheme.typography.labelLarge)
            PrimaryButton(text = "СОЗДАТЬ АККАУНТ", onClick = { component.createAccount() } )
        }
    }
}