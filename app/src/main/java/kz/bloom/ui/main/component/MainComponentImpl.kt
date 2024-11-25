package kz.bloom.ui.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.serialization.Serializable
import kz.bloom.ui.intro.splash.isAccessTokenExpired
import kz.bloom.ui.main.bottom_nav_bar.NavBottomBarComponent
import kz.bloom.ui.main.bottom_nav_bar.NavBottomBarComponentImpl
import kz.bloom.ui.main.bottom_nav_bar.TabItem
import kz.bloom.ui.main.component.MainComponent.Child
import kz.bloom.ui.main.home_page.component.HomePageComponent
import kz.bloom.ui.main.home_page.component.HomePageComponentImpl
import kz.bloom.ui.main.profile.component.ProfileMainComponent
import kz.bloom.ui.main.profile.component.ProfileMainComponentImpl
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


public class MainComponentImpl(
    componentContext: ComponentContext,
    private val onOpenSubscriptions: () -> Unit,
    private val onNeedAuth:() -> Unit
) : MainComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    private val sharedPreferences by inject<SharedPreferencesSetting>()

    val navBarComponent: NavBottomBarComponent = NavBottomBarComponentImpl(
        componentContext = DefaultComponentContext(lifecycle = componentContext.lifecycle),
        onTabSelect = { tab ->
            when (tab) {
                TabItem.HOME -> {
                    navigation.popTo(0)
                }

                TabItem.PROFILE -> {
                    if (sharedPreferences.isAuth() && !isAccessTokenExpired(sharedPreferences.accessToken)) {
                        navigation.pushNew(configuration = Configuration.Profile)
                    } else {
                        onNeedAuth()
                    }
                }
                // Handle other tabs...
                else -> {}
            }
        }
    )

    private val _childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Home,
        handleBackButton = false,
        childFactory = { configuration, componentContext ->
            createChild(
                configuration = configuration,
                componentContext = componentContext
            )
        }
    )

    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child = when (configuration) {
        is Configuration.Home -> Child.Home(
            component = homeComponent(
                componentContext = componentContext
            )
        )

        is Configuration.Profile -> Child.Profile(
            component = profileComponent(
                componentContext = componentContext
            )
        )
    }

    private fun homeComponent(
        componentContext: ComponentContext
    ): HomePageComponent = HomePageComponentImpl(
        componentContext = componentContext
    )

    private fun profileComponent(
        componentContext: ComponentContext
    ): ProfileMainComponent = ProfileMainComponentImpl(
        componentContext = componentContext,
        onOpenSubscriptions = { onOpenSubscriptions() }
    )

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object Home : Configuration

        @Serializable
        data object Profile : Configuration
    }
}