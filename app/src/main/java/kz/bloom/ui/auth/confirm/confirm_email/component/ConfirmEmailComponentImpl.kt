package kz.bloom.ui.auth.confirm.confirm_email.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.sign_up.store.AuthStore
import kz.bloom.ui.auth.sign_up.store.AuthStore.Intent
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent.Model
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class ConfirmEmailComponentImpl(
    private val email: String,
    componentContext: ComponentContext,
    private val openOutcomePage:(kind: OutcomeKind) -> Unit,
    private val onBack:() -> Unit
) : ConfirmEmailComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val _model = MutableValue(
        initialValue = Model(
            code = "",
            codeCanBeRequestedAgain = false
        )
    )
    override val model: Value<Model> = _model

    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()

    private val store = instanceKeeper.getStore {
        AuthStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory
        )
    }

    init {
        startTimer()
    }

    override fun fillEditText(value: String) {
        _model.update { it.copy(code = value) }
    }

    override fun confirmEmail() {
        store.accept(Intent.ValidateReceivedCode(_model.value.code, email))
        openOutcomePage(OutcomeKind.Welcome)
    }

    private fun startTimer() {
        CoroutineScope(mainContext).launch {
            val time = 60000L
            delay(time)
            _model.update { it.copy(codeCanBeRequestedAgain = true) }
        }
    }


    override fun onNavigateBack() {
        onBack()
    }

    override fun codeCanBeRequestedAgain(canBe: Boolean) {
        _model.update { it.copy(codeCanBeRequestedAgain = canBe) }
    }

    override fun sendCodeAgain() {
        store.accept(intent = Intent.ReceiveConfirmCode(email = email))
    }
}