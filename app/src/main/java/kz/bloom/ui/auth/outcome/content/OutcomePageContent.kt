package kz.bloom.ui.auth.outcome.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.outcome.component.OutcomeComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.R
import kz.bloom.ui.ui_components.PrimaryButton


@Composable
fun OutcomePageContent(modifier: Modifier = Modifier, component: OutcomeComponent) {
    val model by component.model.subscribeAsState()
    val outcomeKind = model.outcomeKind
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 108.dp)
            .padding(horizontal = 21.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = when (outcomeKind) {
                is OutcomeKind.Welcome -> "ДОБРО ПОЖАЛОВАТЬ"
                is OutcomeKind.Error -> "ОШИБКА"
                is OutcomeKind.RestoreSuccess -> "ПОЗДРАВЛЯЕМ"
            }
        )
        Text(
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            text = when (outcomeKind) {
                is OutcomeKind.Welcome -> "Вы успешно прошли аутентификацию"
                is OutcomeKind.Error -> "К сожалению, произошла ошибка. Попробуйте снова или обратитесь в службу поддержки, если проблема сохраняется."
                is OutcomeKind.RestoreSuccess -> "Ваш пароль был изменён. Нажмите \"Продолжить\", чтобы войти в систему"
            }
        )
        Image(
            modifier = Modifier.padding(top = 45.dp),
            painter = painterResource(id =
            if (outcomeKind == OutcomeKind.Error) {
                R.drawable.error_sign_red
            } else R.drawable.success_sign_green
            ),
            contentDescription = null
        )
        
        PrimaryButton(
            modifier = Modifier.padding(top = 45.dp),
            text = if (outcomeKind == OutcomeKind.Error) {
                "ПОПРОБОВАТЬ СНОВА"
            } else "ПРОДОЛЖИТЬ",
            onClick = { component.continuePressed() }
        )
    }
}