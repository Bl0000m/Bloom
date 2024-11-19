package kz.bloom.ui.profile.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kz.bloom.ui.profile.component.ProfileRootComponent
import kz.bloom.ui.profile.component.ProfileRootComponent.Child
import kz.bloom.ui.profile.profile_main.content.ProfileMainContent

@Composable
fun ProfileRootContent(component: ProfileRootComponent) {
    Scaffold(
        content = { contentPadding ->
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = slide())
            ) { child ->
                val contentPaddingModifier = Modifier.padding(paddingValues = contentPadding)

                when (val childInstance = child.instance) {
                    is Child.ProfileMain -> ProfileMainContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                }
            }
        }
    )
}