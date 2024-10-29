package kz.bloom.ui.auth.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.confirm_email.component.VerificationGenericComponent
import kz.bloom.ui.auth.confirm_email.component.VerificationGenericComponent.VerificationKind
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun GenericVerificationContent(
    modifier: Modifier = Modifier,
    component: VerificationGenericComponent
) {
    val model by component.model.subscribeAsState()
    val verificationKind = model.kind

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
                    is VerificationKind.CreateNewPass -> "2"
                    is VerificationKind.ForgotPassFillCode -> "3"
                    is VerificationKind.ForgotPassFillEmail -> "4"
                }
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFAAAAAA),
                text = when (verificationKind) {
                    is VerificationKind.ConfirmEmail -> "Мы отправили вам письмо с 4-значным кодом"
                    is VerificationKind.CreateNewPass -> "2"
                    is VerificationKind.ForgotPassFillCode -> "3"
                    is VerificationKind.ForgotPassFillEmail -> "4"
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
                is VerificationKind.CreateNewPass -> "ЭЛЕКТРОННАЯ ПОЧТА"
                is VerificationKind.ForgotPassFillCode -> "ВВЕДИТЕ КОД"
                is VerificationKind.ForgotPassFillEmail -> "ВВЕДИТЕ ПАРОЛЬ"
            },
            placeholder = ""
        )

        PrimaryButton(
            text = if (verificationKind == VerificationKind.ForgotPassFillEmail) {
                "ПРОДОЛЖИТЬ"
            } else "ПОТВЕРДИТЬ",
            onClick = { }
        )
    }
}