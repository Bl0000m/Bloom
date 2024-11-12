package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component

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
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.store.AuthStore
import kz.bloom.ui.auth.store.AuthStore.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponent.Model
import kz.bloom.ui.ui_components.coroutineScope

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
            confirmPassword = ""
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

    private val scope = coroutineScope()

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

    override fun onNavigateBack() {
        onBack()
    }
}