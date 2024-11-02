package kz.bloom.ui.auth.sign_up.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent.VerificationKind
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.Model
import kz.bloom.ui.auth.sign_up.store.AuthStore
import kz.bloom.ui.ui_components.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class SignUpComponentImpl(
    componentContext: ComponentContext,
    private val onCreateAccount:(VerificationKind) -> Unit,
    private val onError:(OutcomeKind) -> Unit,
    private val onNavigateBack:() -> Unit,
) : SignUpComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()

    private val scope = coroutineScope()

    private val store = instanceKeeper.getStore {
        AuthStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory
        )
    }

    private val _model: MutableValue<Model> = MutableValue(
        initialValue = Model(
            name = "",
            email = "",
            phoneNumber = "",
            password = "",
            passwordConfirm = "",
            userAgreesToReceiveInfo = false
        )
    )

    override val model: Value<Model> = _model

    override fun fillName(name: String) {
        _model.update { it.copy(name = name) }
    }

    override fun fillMail(email: String) {
       _model.update { it.copy(email = email) }
    }

    override fun fillPhone(phoneNumber: String) {
        _model.update { it.copy(phoneNumber = phoneNumber) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun fillPasswordConfirm(rePassword: String) {
        _model.update { it.copy(passwordConfirm = rePassword) }
    }

    override fun userDoesAgreeToReceiveInfo(tick: Boolean) {
        _model.update { it.copy(userAgreesToReceiveInfo = tick) }
    }

    override fun createAccount() {
        Log.d("behold", "123")
        store.accept(intent = AuthStore.Intent.CreateAccount(model = _model.value))
        scope.launch {
            delay(timeMillis = 500L)
            store.states.subscribe { state ->
                if (state.accountCreated && !state.isLoading) {
                    onCreateAccount(VerificationKind.ConfirmEmail)
                } else if (!state.accountCreated && !state.isLoading) {
                    onError(OutcomeKind.Error)
                }
            }
        }
    }

    override fun navigateBack() {
        onNavigateBack()
    }
}