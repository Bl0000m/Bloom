package kz.bloom.ui.subscription.subs_list.content

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent

@Composable
fun SubsListContent(modifier: Modifier = Modifier, component: SubsListComponent) {
    Button(onClick = { component.chooseDate() }) {

    }
}