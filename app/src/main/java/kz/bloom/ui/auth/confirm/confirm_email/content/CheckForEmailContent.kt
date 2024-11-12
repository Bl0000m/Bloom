package kz.bloom.ui.auth.confirm.confirm_email.content

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.delay
import kz.bloom.R
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun CheckForEmailContent(
    modifier: Modifier = Modifier,
    component: ConfirmEmailComponent
) {
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
                text = "ПОТВЕРДИТЕ ЭЛЕКТРОННЫЙ АДРЕСС"

            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFAAAAAA),
                text =  "Мы отправили вам письмо с 4-значным кодом"
            )
        }
        LabeledTextField(
            modifier = Modifier.focusRequester(focusRequester),
            singleLine = true,
            labelStyle = MaterialTheme.typography.labelSmall,
            onValueChange = {
                component.fillEditText(value = it)
            },
            label = "ВВЕДИТЕ КОД",
            placeholder = "",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = model.code,
            keyboardActions = KeyboardActions(onDone = { focusRequester.freeFocus() } )
        )
        if (!model.codeCanBeRequestedAgain) {
            ResendTimer(
                onTimerFinish = {
                    component.codeCanBeRequestedAgain(canBe = true)
                }
            )
        } else {
            Text(
                modifier = Modifier.clickable { component.sendCodeAgain() },
                text = "Отправить повторно",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }



        PrimaryButton(
            text = "ПОТВЕРДИТЬ",
            onClick = { component.confirmEmail() }
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ResendTimer(
    totalTime: Long = 60_000L,
    onTimerFinish: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(totalTime) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1000L
        }
        onTimerFinish()
    }

    val minutes = (remainingTime / 1000) / 60
    val seconds = (remainingTime / 1000) % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = "Отправить повторно через:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = timeString,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}