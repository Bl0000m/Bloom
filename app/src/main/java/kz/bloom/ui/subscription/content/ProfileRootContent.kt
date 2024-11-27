package kz.bloom.ui.subscription.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kz.bloom.ui.subscription.component.SubscriptionRootComponent
import kz.bloom.ui.subscription.component.SubscriptionRootComponent.Child
import kz.bloom.ui.subscription.date_picker.content.DatePickerContent
import kz.bloom.ui.subscription.subs_list.content.SubsListContent


@Composable
fun SubscriptionRootContent(component: SubscriptionRootComponent) {
    Scaffold(
        content = { contentPadding ->
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = slide())
            ) { child ->
                val contentPaddingModifier = Modifier.padding(paddingValues = contentPadding)

                when (val childInstance = child.instance) {
                    is Child.SubscriptionList -> SubsListContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.DatePicker -> DatePickerContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                }
            }
        }
    )
}