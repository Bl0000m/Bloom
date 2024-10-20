package kz.bloom.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.main.component.MainComponentImpl
import kz.bloom.ui.main.content.MainContent
import kz.bloom.ui.main.content.SplashMainContentAnimation
import kz.bloom.ui.theme.BloomTheme
import kz.bloom.ui.ui_components.AUTH

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val componentContext = defaultComponentContext()
        setContent {
            BloomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    var isAnimationFinished by remember { mutableStateOf(false) }
                    SplashMainContentAnimation(
                        modifier = Modifier.fillMaxSize(),
                        onAnimationFinish = { isAnimationFinished = true })
                    val component = remember {
                        MainComponentImpl(
                            componentContext = componentContext,
                            onNavigateAuth = { onNavigateAuth() }
                        )
                    }
                    if (isAnimationFinished) {
                        MainContent(component = component)
                    }
                }
            }
        }
    }

    private fun onNavigateAuth() {
        with(Intent()) {
            setClassName(this@MainActivity, AUTH)
            startActivity(this)
        }
        finish()
    }
}