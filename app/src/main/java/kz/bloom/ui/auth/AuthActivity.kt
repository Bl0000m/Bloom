package kz.bloom.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.auth.component.AuthComponentImpl
import kz.bloom.ui.auth.content.AuthContent

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = AuthComponentImpl(
            componentContext = defaultComponentContext()
        )
        setContent {
            AuthContent(component = component)
        }
    }
}