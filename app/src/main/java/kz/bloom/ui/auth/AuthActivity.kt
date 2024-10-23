package kz.bloom.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.auth.sign_up.component.SignUpComponentImpl
import kz.bloom.ui.auth.sign_up.content.SignUpContent
import kz.bloom.ui.theme.BloomTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = SignUpComponentImpl(
            componentContext = defaultComponentContext()
        )
        setContent {
            BloomTheme {
                SignUpContent(component = component)
            }
        }
    }
}