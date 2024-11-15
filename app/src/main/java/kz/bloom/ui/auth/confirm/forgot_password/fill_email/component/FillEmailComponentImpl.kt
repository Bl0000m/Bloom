package kz.bloom.ui.auth.confirm.forgot_password.fill_email.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent.Model
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent.Event
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Label
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Intent
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class FillEmailComponentImpl(
    componentContext: ComponentContext,
    private val navigateBack: () -> Unit,
    private val onContinue:(email: String) -> Unit,
    private val openErrorScreen:(outcomeKind: OutcomeKind) -> Unit
) : FillEmailComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()

    private val store = instanceKeeper.getStore {
        FillEmailStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    private val _model = MutableValue(
        initialValue = Model(
            email = ""
        )
    )
    override val model: Value<Model> = _model

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )

    override fun continueAndGetCode() {
        store.accept(intent = Intent.ReceiveConfirmCode(email = _model.value.email))
        if (store.states.value.serverIsNotResponding) {
            openErrorScreen(OutcomeKind.Error)
        }
        onContinue(_model.value.email)
    }

    override fun fillEmail(email: String) {
        _model.update { it.copy(email = email) }
    }

    override fun onNavigateBack() {
        navigateBack()
    }
}

private fun Flow<Label>.toEvents(): Flow<Event> = map { label ->
    when(label) {
        is Label.ErrorReceived -> {
            Event.DisplaySnackBar(errorMessage = label.message)
        }
    }
}