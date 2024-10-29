package kz.bloom.ui.auth.confirm.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent.VerificationKind
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun GenericVerificationContent(
    modifier: Modifier = Modifier,
    component: VerificationGenericComponent
) {
    val model by component.model.subscribeAsState()
    val verificationKind = model.kind

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 51.dp)
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
                text = when (verificationKind) {
                    is VerificationKind.ConfirmEmail -> "ПОТВЕРДИТЕ ЭЛЕКТРОННЫЙ АДРЕСС"
                    is VerificationKind.ForgotPassFillEmail -> "ЗАБЫЛИ ПАРОЛЬ"
                    is VerificationKind.ForgotPassFillCode -> "ПРОВЕРЬТЕ СВОЮ ПОЧТУ"
                    is VerificationKind.CreateNewPass -> "ПРИДУМАЙТЕ НОВЫЙ ПАРОЛЬ"
                }
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFAAAAAA),
                text = when (verificationKind) {
                    is VerificationKind.ConfirmEmail -> "Мы отправили вам письмо с 4-значным кодом"

                    is VerificationKind.ForgotPassFillEmail -> "Пожалуйста, введите ваш электронный адрес для сброса пароля"
                    is VerificationKind.ForgotPassFillCode -> "Мы отправили ссылку на вашу почту Введите 4-значный код, указанный в письме"
                    is VerificationKind.CreateNewPass -> "Убедитесь, что он отличается от предыдущих для безопасности"
                }
            )
        }
        LabeledTextField(
            value = model.editTextValue,
            onValueChange = {
                component.fillEditText(value = it)
            },
            label = when (verificationKind) {
                is VerificationKind.ConfirmEmail -> "ВВЕДИТЕ КОД"

                is VerificationKind.ForgotPassFillEmail -> "ЭЛЕКТРОННАЯ ПОЧТА"
                is VerificationKind.ForgotPassFillCode -> "ВВЕДИТЕ КОД"
                is VerificationKind.CreateNewPass -> "ВВЕДИТЕ ПАРОЛЬ"
            },
            placeholder = "",
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() } )
        )

        PrimaryButton(
            text = if (verificationKind == VerificationKind.ForgotPassFillEmail) {
                "ПРОДОЛЖИТЬ"
            } else "ПОТВЕРДИТЬ",
            onClick = {
                when(verificationKind) {
                    VerificationKind.ConfirmEmail -> component.confirmEmail()

                    VerificationKind.ForgotPassFillEmail -> component.fillInEmailToRestorePass()
                    VerificationKind.ForgotPassFillCode -> component.fillInCodeToRestorePass()
                    VerificationKind.CreateNewPass -> component.createNewPass()
                }

            }
        )
    }
}