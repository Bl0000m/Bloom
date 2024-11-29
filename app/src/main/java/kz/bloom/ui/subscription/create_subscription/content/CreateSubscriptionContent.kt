package kz.bloom.ui.subscription.create_subscription.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun CreateSubscriptionContent(modifier: Modifier = Modifier, component: CreateSubscriptionComponent) {
    val model = component.model.subscribeAsState()


    Box(modifier = modifier) {
        Column(modifier = Modifier
            .padding(top = 42.dp)
            .padding(horizontal = 21.dp)) {
            Icon(
                modifier = Modifier.clickable { component.onNavigateBack() },
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "СОЗДАТЬ ПОДПИСКУ"
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Придумайте название и выбирайте тип из списка\nдля оформления подписки"
                )
            }
            LabeledTextField(
                value = model.value.subscriptionName,
                onValueChange = { component.fillSubName(name = it)},
                label = "НАЗВАНИЕ ПОДПИСКИ",
                placeholder = ""
            )

            PrimaryButton(
                modifier = Modifier.padding(bottom = 21.dp),
                text = "СОЗДАТЬ ПОДПИСКУ",
                textStyle = MaterialTheme.typography.bodySmall,
                onClick = { component.createSub() }
            )
        }
    }
}