package kz.bloom.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kz.bloom.ui.auth.component.AuthRootComponentImpl
import kz.bloom.ui.auth.content.AuthRootContent
import kz.bloom.ui.theme.BloomTheme

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = AuthRootComponentImpl(
            componentContext = defaultComponentContext(),
            onNavigateBack = { finish() },
            context = this
        )

        setContent {
            BloomTheme {
                AuthRootContent(component = component)
            }
        }
    }
}