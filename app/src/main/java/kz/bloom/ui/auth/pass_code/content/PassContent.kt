package kz.bloom.ui.auth.pass_code.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent

@Composable
fun PassContent(modifier: Modifier = Modifier, component: PassCodeComponent) {
    val model by component.model.subscribeAsState()


}