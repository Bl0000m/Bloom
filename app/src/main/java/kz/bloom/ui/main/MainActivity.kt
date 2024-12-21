package kz.bloom.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import kz.bloom.ui.main.component.MainComponent.Event
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.bloom.ui.auth.AuthActivity
import kz.bloom.ui.main.component.MainComponentImpl
import kz.bloom.ui.main.bottom_nav_bar.BottomNavBar
import kz.bloom.ui.main.bottom_nav_bar.TabItem
import kz.bloom.ui.main.content.MainContent
import kz.bloom.ui.main.content.SplashMainContentAnimation
import kz.bloom.ui.theme.BloomTheme
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
            val restoredTabState = remember { mutableStateOf<String?>(null) }
            var isAnimationFinished by remember { mutableStateOf(false) }
            val mainComponent = remember {
                MainComponentImpl(
                    componentContext = componentContext,
                    onOpenSubscriptions = { openSubscriptions() }
                )
            }
            val navBarComponent = remember { mainComponent.navBarComponent }
            val authLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    restoredTabState.value = mainComponent.rememberTab?.name
                    lifecycle.coroutineScope.launch {
                        restoredTabState.value?.let { restoredTab ->
                            navBarComponent.onOpenRestoredTab(TabItem.valueOf(restoredTab))
                            restoredTabState.value = null
                        }
                    }
                }
            LaunchedEffect(Unit) {
                mainComponent.events.collectLatest { event ->
                    when (event) {
                        is Event.OpenAuth -> {
                            openAuth(authLauncher)
                        }
                    }
                }
            }
            BloomTheme {
                if (isAnimationFinished) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            val selectedTab by navBarComponent.selectedTab.subscribeAsState()
                            BottomNavBar(
                                modifier = Modifier.height(
                                    if (isButtonNavigationEnabled(LocalContext.current))
                                        106.dp else 76.dp
                                ),
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

    private fun openAuth(authLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        val intent = Intent(this, AuthActivity::class.java)
        authLauncher.launch(intent)
    }

    private fun openSubscriptions() {
        with(Intent()) {
            setClassName(this@MainActivity, SUBSCRIPTIONS)
            startActivity(this)
        }
    }
}

fun isButtonNavigationEnabled(context: Context): Boolean {
    return try {
        val mode = Settings.Secure.getInt(context.contentResolver, "navigation_mode")
        mode != 2
    } catch (e: Settings.SettingNotFoundException) {
        false
    }
}