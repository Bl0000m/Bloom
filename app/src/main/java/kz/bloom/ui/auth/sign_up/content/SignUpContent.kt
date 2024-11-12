package kz.bloom.ui.auth.sign_up.content

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.R
import kz.bloom.ui.country_chooser.component.isRussiaOrKazakhstan
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PhoneNumberMaskVisualTransformation
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun SignUpContent(modifier: Modifier, component: SignUpComponent) {

    val model by component.model.subscribeAsState()
    val focusManager = LocalFocusManager.current
    val emailFocusRequest = remember { FocusRequester() }
    val nameFocusRequest = remember { FocusRequester() }
    val phoneNumberFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester() }
    val rePasswordFocusRequest = remember { FocusRequester() }

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
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            LabeledTextField(
                modifier = Modifier
                    .focusRequester(nameFocusRequest)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) component.onNameFocusLost()
                    },
                label = "ИМЯ",
                placeholder = "",
                singleLine = true,
                isError = model.nameErrorOccurred.errorOccurred,
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillName(name = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { emailFocusRequest.requestFocus() }
                ),
                value = model.name
            )
            Text(
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                text = model.nameErrorOccurred.errorText,
                color = MaterialTheme.colorScheme.error
            )
            LabeledTextField(
                modifier = Modifier
                    .focusRequester(emailFocusRequest)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) component.onEmailFocusLost()
                    },
                label = "ЭЛЕКТРОННАЯ ПОЧТА",
                singleLine = true,
                placeholder = "",
                isError = model.emailErrorOccurred.errorOccurred,
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillMail(email = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { phoneNumberFocusRequest.requestFocus() }
                ),
                value = model.email
            )
            Text(
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                text = model.emailErrorOccurred.errorText,
                color = MaterialTheme.colorScheme.error
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column {
                    Row(modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { component.openCountryChooser() }) {
                        Text(
                            text = model.selectedCountry.dialCode,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Icon(
                            modifier = Modifier.padding(start = 10.dp),
                            painter = painterResource(id = R.drawable.ic_expand_left_light),
                            contentDescription = null)
                    }
                    HorizontalDivider(
                        modifier = Modifier.width(50.dp).padding(top = 15.dp),
                        color = if (model.phoneNumberErrorOccurred.errorOccurred)
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.secondary
                    )
                }
                
                LabeledTextField(
                    modifier = Modifier
                        .focusRequester(phoneNumberFocusRequest)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) component.onPhoneFocusLost()
                        },
                    label = "НОМЕР ТЕЛЕФОНА",
                    isError = model.phoneNumberErrorOccurred.errorOccurred,
                    singleLine = true,
                    placeholder = null,
                    labelStyle = MaterialTheme.typography.labelSmall,
                    onValueChange = { component.fillPhone(phoneNumber = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequest.requestFocus() }
                    ),
                    visualTransformation = if (model.selectedCountry.isRussiaOrKazakhstan) {
                        PhoneNumberMaskVisualTransformation(length = 10)
                    } else VisualTransformation.None,
                    value = model.phoneNumber
                )
            }

            Text(
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                text = model.phoneNumberErrorOccurred.errorText,
                color = MaterialTheme.colorScheme.error
            )

            LabeledTextField(
                modifier = Modifier
                    .focusRequester(passwordFocusRequest)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) component.onPasswordFocusLost()
                    },
                label = "ПАРОЛЬ",
                singleLine = true,
                isError = model.passwordErrorOccurred.errorOccurred,
                placeholder = "",
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.fillPassword(password = it) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = { rePasswordFocusRequest.requestFocus() }
                ),
                value = model.password
            )

            Text(
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                text = model.passwordErrorOccurred.errorText,
                color = MaterialTheme.colorScheme.error
            )

            LabeledTextField(
                modifier = Modifier
                    .focusRequester(rePasswordFocusRequest)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) component.onConfirmPasswordFocusLost()
                    },
                label = "ПОВТОРИТЕ ПАРОЛЬ",
                isError = model.confirmPasswordErrorOccurred.errorOccurred,
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

            Text(
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                text = model.confirmPasswordErrorOccurred.errorText,
                color = MaterialTheme.colorScheme.error
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