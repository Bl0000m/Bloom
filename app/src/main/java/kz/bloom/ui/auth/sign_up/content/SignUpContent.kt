package kz.bloom.ui.auth.sign_up.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.R
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun SignUpContent(modifier: Modifier, component: SignUpComponent) {

    val model by component.model.subscribeAsState()
    val focusManager = LocalFocusManager.current
    val emailFocusRequest = remember { FocusRequester() }
    val nameFocusRequest = remember { FocusRequester () }
    val phoneNumberFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester () }
    val rePasswordFocusRequest = remember { FocusRequester () }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 51.dp)
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
                text = "СОЗДАТЬ АККАУНТ",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(
            modifier = Modifier.padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LabeledTextField(
                modifier = Modifier.focusRequester(nameFocusRequest),
                label = "ИМЯ",
                placeholder = "",
                singleLine = true,
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillName(name = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { emailFocusRequest.requestFocus() }
                ),
                value = model.name
            )
            LabeledTextField(
                modifier = Modifier.focusRequester(emailFocusRequest),
                label = "ЭЛЕКТРОННАЯ ПОЧТА",
                singleLine = true,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = {component.fillMail(email = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { phoneNumberFocusRequest.requestFocus() }
                ),
                value = model.email
            )

            LabeledTextField(
                modifier = Modifier.focusRequester(phoneNumberFocusRequest),
                label = "НОМЕР ТЕЛЕФОНА",
                singleLine = true,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPhone(phoneNumber = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequest.requestFocus() }
                ),
                value = model.phoneNumber
            )


            LabeledTextField(
                modifier = Modifier.focusRequester(passwordFocusRequest),
                label = "ПАРОЛЬ",
                singleLine = true,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPassword(password = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = { rePasswordFocusRequest.requestFocus() }
                ),
                value = model.password
            )

            LabeledTextField(
                modifier = Modifier.focusRequester(rePasswordFocusRequest),
                label = "ПОВТОРИТЕ ПАРОЛЬ",
                singleLine = true,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPasswordConfirm(rePassword = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                value = model.passwordConfirm
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(44.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.size(size = 28.dp),
                    checked = model.userAgreesToReceiveInfo,
                    onCheckedChange = { component.userDoesAgreeToReceiveInfo(tick = it) }
                )
                Text(
                    modifier = Modifier.padding(),
                    text = "Я хочу получать информацию о приложений на электронную почту",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            PrimaryButton(text = "СОЗДАТЬ АККАУНТ", textStyle = MaterialTheme.typography.bodySmall, onClick = { component.createAccount() } )
        }
    }
}