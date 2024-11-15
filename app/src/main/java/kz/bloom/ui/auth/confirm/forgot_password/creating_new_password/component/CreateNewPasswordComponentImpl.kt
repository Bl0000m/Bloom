package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponent.Model
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponent.Event
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Label
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.ErrorBody
import kz.bloom.ui.auth.sign_up.component.updateFieldErrorOnSecondFocusLost
import kz.bloom.ui.auth.sign_up.component.validateConfirmPassword
import kz.bloom.ui.auth.sign_up.component.validatePassword
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting

class CreateNewPasswordComponentImpl(
    componentContext: ComponentContext,
    private val email: String,
    private val openOutcomePage:(kind: OutcomeKind) -> Unit,
    private val onBack:() -> Unit
) : CreateNewPasswordComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val _model = MutableValue(
        initialValue = Model(
            password = "",
            confirmPassword = "",
            passwordErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            confirmPasswordErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            )
        )
    )
    override val model: Value<Model> = _model

    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()

    private val store = instanceKeeper.getStore {
        CreateNewPasswordStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    private val scope = coroutineScope()

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun fillConfirmPassword(confirmPassword: String) {
        _model.update { it.copy(confirmPassword = confirmPassword) }
    }

    override fun createNewPass() {
        scope.launch {
            store.accept(intent = Intent.CreateNewPass(
                email = email,
                password = _model.value.password,
                confirmPassword = _model.value.confirmPassword)
            )
            delay(500)
            if (store.states.value.newPassCreated) {
                openOutcomePage(OutcomeKind.RestoreSuccess)
            }
        }
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

    override fun onConfirmPasswordFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.confirmPasswordErrorOccurred,
            validateField = { validateConfirmPassword(_model.value.password, _model.value.confirmPassword) },
            didErrorOccur = { validateConfirmPassword(_model.value.password, _model.value.confirmPassword) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(confirmPasswordErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onNavigateBack() {
        onBack()
    }
}

private fun Flow<Label>.toEvents(): Flow<Event> = map { label ->
    when(label) {
        is Label.ErrorReceived -> {
            Event.DisplaySnackBar(errorMessage = label.message)
        }
    }
}