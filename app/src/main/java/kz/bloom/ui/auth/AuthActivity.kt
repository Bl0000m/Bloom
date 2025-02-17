package kz.bloom.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.auth.component.AuthRootComponentImpl
import kz.bloom.ui.auth.content.AuthRootContent
import kz.bloom.ui.theme.BloomTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = AuthRootComponentImpl(
            componentContext = defaultComponentContext()
        )
        setContent {
            BloomTheme {
                AuthRootContent(component = component)
            }
        }
    }
}