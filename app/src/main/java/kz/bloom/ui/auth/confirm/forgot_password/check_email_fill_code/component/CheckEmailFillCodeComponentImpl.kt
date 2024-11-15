package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Label
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponent.Model
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponent.Event
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting

class CheckEmailFillCodeComponentImpl(
    componentContext: ComponentContext,
    private val email: String,
    private val onContinue:() -> Unit,
    private val onBack:() -> Unit
) : CheckEmailFillCodeComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()
    private val scope = coroutineScope()

    private val _model = MutableValue(
        initialValue = Model(
            code = "",
            codeCanBeRequestedAgain = false
        )
    )
    override val model: Value<Model> = _model

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    private val store = instanceKeeper.getStore {
        CheckEmailFillCodeStore(
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

    init {
        startTimer()
    }

    override fun fillEditText(value: String) {
        _model.update { it.copy(code = value) }
    }

    override fun sendCode() {
        store.accept(Intent.ValidateReceivedCode( email = email, code = _model.value.code))
        scope.launch {
            delay(timeMillis = 400)
            if (store.states.value.confirmCodeSent) {
                onContinue()
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
        is Label.ReceivedError -> {
            Event.DisplaySnackBar(errorMessage = label.message)
        }
    }
}