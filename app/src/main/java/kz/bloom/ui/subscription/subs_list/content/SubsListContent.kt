package kz.bloom.ui.subscription.subs_list.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun SubsListContent(modifier: Modifier = Modifier, component: SubsListComponent) {
    val model = component.model.subscribeAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .padding(top = 42.dp)
            .padding(horizontal = 21.dp)) {
            Icon(
                modifier = Modifier.clickable { component.onBackPressed() },
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "МОИ ПОДПИСКИ"
                )
                Spacer(modifier = modifier.height(height = 93.dp))

                Icon(painter = painterResource(id = R.drawable.ic_check_ring), contentDescription = null)
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    style = MaterialTheme.typography.labelSmall,
                    text = "У ВАС ПОКА НЕТ АКТИВНЫХ ПОДПИСОК"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Как только вы добавите подписки, они будут \nотображаться здесь."
                )
            }
        }
        PrimaryButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            text = "ДОБАВИТЬ",
            isAlternative = true,
            onClick = { component.createSubscription() }
        )
    }
}