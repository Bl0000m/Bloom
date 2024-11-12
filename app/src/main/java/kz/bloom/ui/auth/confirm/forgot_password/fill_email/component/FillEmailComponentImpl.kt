package kz.bloom.ui.auth.confirm.forgot_password.fill_email.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent.Model
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.store.AuthStore.Intent
import kz.bloom.ui.auth.store.AuthStore
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
        AuthStore(
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