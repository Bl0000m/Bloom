package kz.bloom.ui.auth.component

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.auth.component.AuthRootComponent.Model
import kz.bloom.ui.auth.component.AuthRootComponent.Child
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponentImpl
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponent
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponentImpl
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponentImpl
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponentImpl
import kz.bloom.ui.auth.outcome.component.OutcomeComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.outcome.component.OutcomeComponentImpl
import kz.bloom.ui.auth.pass_code.component.PassComponentImpl
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponentImpl
import kz.bloom.ui.auth.sign_up.component.SignUpComponentImpl
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

        is Configuration.ConfirmEmail -> Child.ConfirmEmail(
            component = confirmEmailComponent(
                componentContext = componentContext,
                email = configuration.email
            )
        )

        is Configuration.SignUp -> {
            currentSignUpComponent = currentSignUpComponent ?: signUpComponent(
                componentContext = componentContext,
                selectedCountry = configuration.selectedCountry
            )
            Child.SignUp(component = currentSignUpComponent!!)
        }

        is Configuration.FPFillEmail -> Child.FillEmail(
            component = fillEmailComponent(
                componentContext = componentContext
            )
        )

        is Configuration.FPCheckEmailFillCode -> Child.CheckEmailFillCode(
            component = checkEmailFillCodeComponent(
                componentContext = componentContext,
                email = configuration.email
            )
        )

        is Configuration.FPCreateNewPass -> Child.CreateNewPass(
            component = createNewPassComponent(
                componentContext = componentContext,
                email = configuration.email
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

    private fun createNewPassComponent(
        componentContext: ComponentContext,
        email: String
    ) : CreateNewPasswordComponent = CreateNewPasswordComponentImpl(
        componentContext = componentContext,
        email = email,
        openOutcomePage = { kind -> navigation.pushNew(configuration = Configuration.Outcome(kind)) },
        onBack = { navigation.pop() }
    )

    private fun checkEmailFillCodeComponent(
        componentContext: ComponentContext,
        email: String
    ) : CheckEmailFillCodeComponent = CheckEmailFillCodeComponentImpl(
        componentContext = componentContext,
        email = email,
        onBack = { navigation.pop() },
        onContinue = { navigation.pushNew(configuration = Configuration.FPCreateNewPass(email))}
    )

    private fun fillEmailComponent(
        componentContext: ComponentContext
    ) : FillEmailComponent = FillEmailComponentImpl(
        componentContext = componentContext,
        onContinue = { navigation.pushNew(configuration = Configuration.FPCheckEmailFillCode(email = it))},
        navigateBack = { navigation.pop() },
        openErrorScreen = { errorKind -> navigation.pushNew(configuration = Configuration.Outcome(errorKind))}
    )

    private fun confirmEmailComponent(
        componentContext: ComponentContext,
        email: String
    ) : ConfirmEmailComponent = ConfirmEmailComponentImpl(
        componentContext = componentContext,
        email = email,
        openOutcomePage = { kind -> navigation.pushNew(configuration = Configuration.Outcome(kind)) },
        onBack = { navigation.pop() }
    )

    private fun signInComponent(
        componentContext: ComponentContext,
    ) : SignInComponent = SignInComponentImpl(
        componentContext = componentContext,
        onCreateAccount = { navigation.pushNew(configuration = Configuration.SignUp(null)) },
        onNavigateBack = onNavigateBack,
        onForgotPassword = {
            navigation.pushNew(
                configuration = Configuration.FPFillEmail
            )
        }
    )

    private fun signUpComponent(
        componentContext: ComponentContext,
        selectedCountry: CountryModel?
    ) : SignUpComponent = SignUpComponentImpl(
        componentContext = componentContext,
        selectedCountry = selectedCountry,
        onCreateAccount = { email ->
            navigation.pushNew(
                configuration =
                Configuration.ConfirmEmail(email = email)
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

    private fun outcomeComponent(
        componentContext: ComponentContext,
        outcomeKind: OutcomeKind,
    ) : OutcomeComponent = OutcomeComponentImpl(
        componentContext = componentContext,
        outcomeKind = outcomeKind,
        onOpenPass = {
            navigation.pushNew(configuration =
            Configuration.PassCode(userHasPinCode = false)
            )
        },
        onOpenSignIn = { navigation.popToFirst() }
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
        data class ConfirmEmail(val email: String) : Configuration
        @Serializable
        data class FPCheckEmailFillCode(val email: String) : Configuration
        @Serializable
        data object FPFillEmail : Configuration
        @Serializable
        data class FPCreateNewPass(val email: String) : Configuration
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