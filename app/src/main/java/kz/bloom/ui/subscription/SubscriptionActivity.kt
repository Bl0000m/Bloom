package kz.bloom.ui.subscription

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kz.bloom.ui.subscription.component.SubscriptionRootComponentImpl
import kz.bloom.ui.subscription.content.SubscriptionRootContent
import kz.bloom.ui.theme.BloomTheme
import org.koin.core.component.KoinComponent

class SubscriptionActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = SubscriptionRootComponentImpl(
            componentContext = defaultComponentContext(),
            onNavigateBack = { finish() }
        )
        setContent {
            BloomTheme {
                SubscriptionRootContent(component = component)
            }
        }
    }
}