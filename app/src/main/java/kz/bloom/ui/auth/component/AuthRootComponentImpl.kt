package kz.bloom.ui.auth.component

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.core.content.ContentProviderCompat.requireContext
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
import kz.bloom.ui.auth.sign_in.component.SignInComponentImpl
import kz.bloom.ui.auth.sign_up.component.SignUpComponentImpl
import kz.bloom.ui.country_chooser.AssetsProvider
import kz.bloom.ui.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.country_chooser.component.ChooseCountryComponentImpl
import kz.bloom.ui.country_chooser.component.CountryModel
import kz.bloom.ui.ui_components.parseAssetsFileContents
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
    private val startingCountryModel = CountryModel(
        code = "KZ",
        name = "Казахстан",
        dialCode = "+7",
        flagEmoji = "\uD83C\uDDF0\uD83C\uDDFF",
        phoneNumberLength = 10
    )

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

        is Configuration.SignUp -> Child.SignUp(
            component = signUpComponent(
                componentContext = componentContext,
                selectedCountry = configuration.selectedCountry ?: startingCountryModel
            )
        )

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
                componentContext = componentContext
            )
        )
    }

    private fun signInComponent(
        componentContext: ComponentContext
    ): SignInComponent = SignInComponentImpl(
        componentContext = componentContext,
        onCreateAccount = { navigation.pushNew(configuration = Configuration.SignUp(startingCountryModel)) },
        onNavigateBack = onNavigateBack,
        onForgotPassword = {
            navigation.pushNew(
                configuration = Configuration.GenericConfirm(VerificationKind.ForgotPassFillEmail)
            )
        }
    )

    private fun signUpComponent(
        componentContext: ComponentContext,
        selectedCountry: CountryModel
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
        onOpenCountryChooser = { selectedCountry ->
            navigation.pushNew(
                configuration = Configuration.ChooseCountry(selectedCountry)
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
        onContinue = { }
    )

    private fun chooseCountryComponent(
        componentContext: ComponentContext
    ) : ChooseCountryComponent = ChooseCountryComponentImpl(
        componentContext = componentContext,
        onCountrySelected = { selectedCountry ->
            navigation.pushNew(configuration = Configuration.SignUp(selectedCountry))
        },
        onNavigateBack = { navigation.pop() },
        context = context
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
        data class ChooseCountry(
            val selectedCountry: CountryModel
        ) : Configuration
    }

}