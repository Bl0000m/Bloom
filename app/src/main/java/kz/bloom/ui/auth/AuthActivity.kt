package kz.bloom.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.auth.component.AuthComponentImpl
import kz.bloom.ui.auth.content.AuthContent
import kz.bloom.ui.theme.BloomTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = AuthComponentImpl(
            componentContext = defaultComponentContext()
        )
        setContent {
            BloomTheme {
                AuthContent(component = component)
            }
        }
    }
}