package kz.bloom.ui.main.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kz.bloom.ui.main.component.MainComponent
import kz.bloom.ui.main.component.MainComponent.Child
import kz.bloom.ui.main.home_page.content.HomePageContent
import kz.bloom.ui.main.profile.content.ProfileMainContent

@Composable
fun MainContent(component: MainComponent) {
    Scaffold(
        content = { contentPadding ->
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = slide())
            ) { child ->
                val contentPaddingModifier = Modifier.padding(paddingValues = contentPadding)

                when(val childInstance = child.instance) {
                    is Child.Home -> HomePageContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.Profile -> ProfileMainContent(
                        component = childInstance.component
                    )
                }
            }
        }
    )
}
