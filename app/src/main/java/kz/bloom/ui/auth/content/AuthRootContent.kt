package kz.bloom.ui.auth.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kz.bloom.ui.auth.component.AuthRootComponent
import kz.bloom.ui.auth.component.AuthRootComponent.Child
import kz.bloom.ui.auth.sign_in.content.SignInContent
import kz.bloom.ui.auth.sign_up.content.SignUpContent


@Composable
fun AuthRootContent(component: AuthRootComponent) {
    Scaffold (
        content = { contentPadding ->
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = slide())
            ) { child ->
                val contentPaddingModifier = Modifier.padding(paddingValues = contentPadding)

                when (val childInstance = child.instance) {
                    is Child.SignIn -> SignInContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.SignUp -> SignUpContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                }
            }
        }
    )


}