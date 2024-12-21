package kz.bloom.ui.auth

import android.content.Intent
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
            componentContext = defaultComponentContext(),
            onNavigateBack = { finish() },
            context = this,
            onTokenUpdated = {
                val resultIntent = Intent().apply {
                    putExtra("EXTRA_AUTH_SUCCESS", true)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        )

        setContent {
            BloomTheme {
                AuthRootContent(component = component)
            }
        }
    }
}