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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.main.component.MainComponentImpl
import kz.bloom.ui.main.bottom_nav_bar.BottomNavBar
import kz.bloom.ui.main.content.MainContent
import kz.bloom.ui.main.content.SplashMainContentAnimation
import kz.bloom.ui.theme.BloomTheme
import kz.bloom.ui.ui_components.AUTH
import kz.bloom.ui.ui_components.SUBSCRIPTIONS
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    private val sharedPreferences by inject<SharedPreferencesSetting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val componentContext = defaultComponentContext()

        setContent {
            BloomTheme {
                var isAnimationFinished by remember { mutableStateOf(false) }
                val mainComponent = remember {
                    MainComponentImpl(
                        componentContext = componentContext,
                        onOpenSubscriptions = { openSubscriptions() },
                        onNeedAuth = { openAuth() }
                    )
                }
                val navBarComponent = remember { mainComponent.navBarComponent }

                if (isAnimationFinished) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            val selectedTab by navBarComponent.selectedTab.subscribeAsState()
                            BottomNavBar(
                                selectedTab = selectedTab,
                                onTabSelected = { navBarComponent.onTabSelected(it) }
                            )
                        },
                        content = { _ ->
                            MainContent(
                                component = mainComponent
                            )
                        }
                    )
                } else {
                    SplashMainContentAnimation(
                        modifier = Modifier.fillMaxSize(),
                        onAnimationFinish = { isAnimationFinished = true }
                    )
                }
            }
        }
    }

    private fun openAuth() {
        with(Intent()) {
            setClassName(this@MainActivity, AUTH)
            startActivity(this)
        }
    }
    private fun openSubscriptions() {
        with(Intent()) {
            setClassName(this@MainActivity, SUBSCRIPTIONS)
            startActivity(this)
        }
    }
}