package kz.bloom.ui.profile.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import kz.bloom.ui.profile.component.ProfileRootComponent.Model
import kz.bloom.ui.profile.component.ProfileRootComponent.Child
import kz.bloom.ui.profile.profile_main.component.ProfileMainComponent
import kz.bloom.ui.profile.profile_main.component.ProfileMainComponentImpl

class ProfileRootComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val onOpenSubscriptions: () -> Unit
) : ProfileRootComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val _model = MutableValue(
        initialValue = Model(
            smth = false
        )
    )

    override val model: Value<Model> = _model

    private val navigation = StackNavigation<Configuration>()

    private val _childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.ProfileMain,
        handleBackButton = true,
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
        is Configuration.ProfileMain -> Child.ProfileMain(
            component = profileMainComponent(
                componentContext = componentContext
            )
        )
    }

    private fun profileMainComponent(
        componentContext: ComponentContext
    ) : ProfileMainComponent = ProfileMainComponentImpl(
        componentContext = componentContext,
        onOpenSubscriptions =  { onOpenSubscriptions() }
    )

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object ProfileMain : Configuration
    }
}