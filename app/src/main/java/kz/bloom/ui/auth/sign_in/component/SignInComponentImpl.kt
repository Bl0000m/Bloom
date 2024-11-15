package kz.bloom.ui.auth.sign_in.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kz.bloom.ui.auth.api.AuthApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Model
import kz.bloom.ui.auth.sign_in.store.SignInStore
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Event
import kz.bloom.ui.auth.sign_in.store.SignInStore.Label
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.ErrorBody
import kz.bloom.ui.auth.sign_up.component.updateFieldErrorOnSecondFocusLost
import kz.bloom.ui.auth.sign_up.component.validateEmail
import kz.bloom.ui.auth.sign_up.component.validatePassword

class SignInComponentImpl(
    componentContext: ComponentContext,
    private val onCreateAccount:() -> Unit,
    private val onNavigateBack:() -> Unit,
    private val onAccountEntered:() -> Unit,
    private val onForgotPassword:() -> Unit
): SignInComponent,
   KoinComponent,
   ComponentContext by componentContext
{
    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()

    private val _model = MutableValue(
        initialValue = Model(
            email = "",
            password = "",
            ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
        )
    )
    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    private val scope = coroutineScope()

    private val store = instanceKeeper.getStore {
        SignInStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )


    override val model: Value<Model> = _model

    override fun fillEmail(email: String) {
        _model.update { it.copy(email = email) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun enterAccount() {
        store.accept(intent = SignInStore.Intent.EnterAccount(model = _model.value))
        scope.launch {
            delay(500)
            if(sharedPreferences.isAuth()) {
                onAccountEntered()
            }
        }
    }

    override fun createAccount() {
        onCreateAccount()
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    override fun forgotPassword() {
        onForgotPassword()
    }

    override fun onEmailFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.emailErrorOccurred,
            validateField = { validateEmail(_model.value.email) },
            didErrorOccur = { validateEmail(_model.value.email) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(emailErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onPasswordFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.passwordErrorOccurred,
            validateField = { validatePassword(_model.value.password) },
            didErrorOccur = { validatePassword(_model.value.password) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(passwordErrorOccurred = updatedErrorBody) }
            }
        )
    }

    private fun Flow<Label>.toEvents(): Flow<Event> = map { label ->
        when(label) {
            is Label.ReceivedError -> {
                Event.DisplaySnackBar(errorMessage = label.message)
            }
        }
    }
}