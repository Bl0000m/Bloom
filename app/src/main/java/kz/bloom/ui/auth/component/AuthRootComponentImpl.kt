package kz.bloom.ui.auth.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.auth.component.AuthRootComponent.Model
import kz.bloom.ui.auth.component.AuthRootComponent.Child
import kz.bloom.ui.auth.sign_in.component.SignInComponentImpl
import kz.bloom.ui.auth.sign_up.component.SignUpComponentImpl
import org.koin.core.component.KoinComponent

internal class AuthRootComponentImpl(
    componentContext: ComponentContext
) : AuthRootComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val _model = MutableValue(
        initialValue = Model(
            smth = true
        )
    )

    private val navigation = StackNavigation<Configuration>()

    private val _childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SignUp,
        handleBackButton = true,
        childFactory = { configuration, componentContext ->
            createChild(
                configuration = configuration,
                componentContext = componentContext
            )
        }
    )

    override val model: Value<Model> = _model

    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ) : Child = when(configuration) {
        is Configuration.SignIn -> Child.SignIn(
            component = signInComponent(
                componentContext = componentContext
            )
        )

        is Configuration.SignUp -> Child.SignUp(
            component = signUpComponent(
                componentContext = componentContext
            )
        )
    }

    private fun signInComponent(
        componentContext: ComponentContext
    ): SignInComponent = SignInComponentImpl(
        componentContext = componentContext
    )

    private fun signUpComponent(
        componentContext: ComponentContext
    ) : SignUpComponent = SignUpComponentImpl(
        componentContext = componentContext,
        onLoginPage = { navigation.pushNew(configuration = Configuration.SignIn) }
    )

    @Serializable
    private sealed interface Configuration {
        @Serializable
        object SignIn : Configuration
        @Serializable
        object SignUp : Configuration
    }

}