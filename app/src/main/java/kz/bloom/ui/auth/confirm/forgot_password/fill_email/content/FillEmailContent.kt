package kz.bloom.ui.auth.confirm.forgot_password.fill_email.content

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent.Model
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun FillEmailContent(modifier: Modifier = Modifier, component: FillEmailComponent) {
    val model by component.model.subscribeAsState()

    val focusRequester = remember { FocusRequester() }

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
            label = "ВВЕДИТЕ КОД",
            placeholder = "",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
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
}
