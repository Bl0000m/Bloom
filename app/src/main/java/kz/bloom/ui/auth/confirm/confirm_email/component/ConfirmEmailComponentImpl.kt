package kz.bloom.ui.auth.confirm.confirm_email.component

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
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore.Intent
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent.Model
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent.Event
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore.Label
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class ConfirmEmailComponentImpl(
    private val email: String,
    componentContext: ComponentContext,
    private val openOutcomePage:(kind: OutcomeKind) -> Unit,
    private val onBack:() -> Unit,
    private val openErrorPage:() -> Unit
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
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()
    private val scope = coroutineScope()

    private val store = instanceKeeper.getStore {
        ConfirmEmailStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )

    init {
        startTimer()
    }

    override fun fillEditText(value: String) {
        _model.update { it.copy(code = value) }
    }

    override fun confirmEmail() {
        store.accept(intent = Intent.ValidateReceivedCode(code = _model.value.code, email = email))
        scope.launch {
            delay(400)
            if (store.states.value.confirmCodeSent) {
                openOutcomePage(OutcomeKind.Welcome)
            } else if(store.states.value.serverIsNotResponding) {
                openErrorPage()
            }
        }
    }

    private fun startTimer() {
        CoroutineScope(mainContext).launch {
            val time = 60000L
            delay(time)
            _model.update { it.copy(codeCanBeRequestedAgain = true) }
        }
    }


    override fun codeCanBeRequestedAgain(canBe: Boolean) {
        _model.update { it.copy(codeCanBeRequestedAgain = canBe) }
    }

    override fun sendCodeAgain() {
        store.accept(intent = Intent.ReceiveConfirmCode(email = email))
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