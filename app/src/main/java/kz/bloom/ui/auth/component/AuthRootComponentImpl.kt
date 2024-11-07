package kz.bloom.ui.auth.component

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.auth.component.AuthRootComponent.Model
import kz.bloom.ui.auth.component.AuthRootComponent.Child
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent.VerificationKind
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponentImpl
import kz.bloom.ui.auth.outcome.component.OutcomeComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.outcome.component.OutcomeComponentImpl
import kz.bloom.ui.auth.pass_code.component.PassComponentImpl
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponentImpl
import kz.bloom.ui.auth.sign_up.component.SignUpComponentImpl
import kz.bloom.ui.country_chooser.AssetsProvider
import kz.bloom.ui.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.country_chooser.component.ChooseCountryComponentImpl
import kz.bloom.ui.country_chooser.component.CountryModel
import org.koin.core.component.KoinComponent

internal class AuthRootComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack:() -> Unit,
    private val context: Context
) : AuthRootComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val _model = MutableValue(
        initialValue = Model(
            smth = true
        )
    )

    private val isSuccess = MutableValue(true)

    private var currentSignUpComponent: SignUpComponent? = null

    private val navigation = StackNavigation<Configuration>()

    private val _childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SignIn,
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

        is Configuration.SignUp -> {
            currentSignUpComponent = currentSignUpComponent ?: signUpComponent(
                componentContext = componentContext,
                selectedCountry = configuration.selectedCountry
            )
            Child.SignUp(component = currentSignUpComponent!!)
        }

        is Configuration.GenericConfirm -> Child.ConfirmEmail(
            component = genericConfirmComponent(
                componentContext = componentContext,
                confirmKind = configuration.confirmKind
            )
        )

        is Configuration.Outcome -> Child.OutcomePage(
            component = outcomeComponent(
                componentContext = componentContext,
                outcomeKind = configuration.outcomeKind
            )
        )
        is Configuration.ChooseCountry -> Child.CountryChoose(
            component = chooseCountryComponent(
                componentContext = componentContext,
                onCountrySelected = { selectedCountry ->
                    currentSignUpComponent?.updateSelectedCountry(selectedCountry)
                    navigation.pop()
                }
            )
        )
        is Configuration.PassCode -> Child.PassCode(
            component = passCodeComponent(
                componentContext = componentContext,
                userHasPinCode = configuration.userHasPinCode
            )
        )
    }

    private fun signInComponent(
        componentContext: ComponentContext,
    ) : SignInComponent = SignInComponentImpl(
        componentContext = componentContext,
        onCreateAccount = { navigation.pushNew(configuration = Configuration.SignUp(null)) },
        onNavigateBack = onNavigateBack,
        onForgotPassword = {
            navigation.pushNew(
                configuration = Configuration.GenericConfirm(VerificationKind.ForgotPassFillEmail)
            )
        }
    )

    private fun signUpComponent(
        componentContext: ComponentContext,
        selectedCountry: CountryModel?
    ) : SignUpComponent = SignUpComponentImpl(
        componentContext = componentContext,
        selectedCountry = selectedCountry,
        onCreateAccount = { confirmEmailVerify ->
            navigation.pushNew(
                configuration =
                Configuration.GenericConfirm(confirmEmailVerify)
            )
        },
        onError = { errorOutcome ->
            navigation.pushNew(
                configuration =
                Configuration.Outcome(errorOutcome)
            )
        },
        onNavigateBack = { navigation.pop() },
        onOpenCountryChooser = {
            navigation.pushNew(
                configuration = Configuration.ChooseCountry
            )
        }
    )

    private fun genericConfirmComponent(
        componentContext: ComponentContext,
        confirmKind: VerificationKind,
    ) : VerificationGenericComponent = VerificationGenericComponentImpl(
        componentContext = componentContext,
        kind = confirmKind,
        onBack = { navigation.pop() },
        openOutcomePage = {  outcomeKind ->
            navigation.pushNew(
                configuration = Configuration.Outcome(
                    outcomeKind = if (isSuccess.value) {
                        outcomeKind
                    } else OutcomeKind.Error
                )
            )
        }
    )

    private fun outcomeComponent(
        componentContext: ComponentContext,
        outcomeKind: OutcomeKind,
    ) : OutcomeComponent = OutcomeComponentImpl(
        componentContext = componentContext,
        outcomeKind = outcomeKind,
        onNavigateBack = { },
        onContinue = { callBackOutcome->
            when (callBackOutcome) {
                is OutcomeKind.Welcome -> {
                    navigation.pushNew(configuration = Configuration.PassCode(userHasPinCode = false))
                }

                is OutcomeKind.RestoreSuccess -> {

                }

                is OutcomeKind.Error -> {

                }
            }
        }
    )

    private fun chooseCountryComponent(
        componentContext: ComponentContext,
        onCountrySelected: (CountryModel) -> Unit,
    ) : ChooseCountryComponent = ChooseCountryComponentImpl(
        componentContext = componentContext,
        onCountrySelected = { selectedCountry ->
            onCountrySelected(selectedCountry)
        },
        onNavigateBack = { navigation.pop() },
        context = context
    )

    private fun passCodeComponent(
        componentContext: ComponentContext,
        userHasPinCode: Boolean
    ) : PassCodeComponent = PassComponentImpl(
        componentContext = componentContext,
        userHasPinCode = userHasPinCode,
        onNavigateBack = { onNavigateBack() }
    )

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object SignIn : Configuration
        @Serializable
        data class SignUp(
            val selectedCountry: CountryModel?
        ) : Configuration
        @Serializable
        data class GenericConfirm(
            val confirmKind: VerificationKind
        ) : Configuration
        @Serializable
        data class Outcome(
            val outcomeKind: OutcomeKind
        ) : Configuration
        @Serializable
        data object ChooseCountry : Configuration
        @Serializable
        data class PassCode(
            val userHasPinCode: Boolean
        ) : Configuration
    }
}