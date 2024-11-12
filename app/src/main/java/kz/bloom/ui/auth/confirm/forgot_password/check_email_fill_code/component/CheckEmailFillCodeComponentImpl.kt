package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component

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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponent.Model

class CheckEmailFillCodeComponentImpl(
    componentContext: ComponentContext,
    private val email: String,
    private val onContinue:() -> Unit,
    private val onBack:() -> Unit
) : CheckEmailFillCodeComponent,
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

    override fun fillEditText(value: String) {
        _model.update { it.copy(code = value) }
    }


    override fun sendCode() {
        store.accept(Intent.ValidateReceivedCode(_model.value.code, email))
        onContinue()
    }


    override fun onNavigateBack() {
        onBack()
    }

}