package kz.bloom.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.profile.component.ProfileRootComponentImpl
import kz.bloom.ui.profile.content.ProfileRootContent
import kz.bloom.ui.theme.BloomTheme
import org.koin.core.component.KoinComponent

class ProfileActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = ProfileRootComponentImpl(
            componentContext = defaultComponentContext(),
            onOpenSubscriptions = { },
            onNavigateBack = { finish() }
        )
        setContent {
            BloomTheme {
                ProfileRootContent(component = component)
            }
        }
    }
}