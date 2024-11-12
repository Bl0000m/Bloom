package kz.bloom.ui.auth.sign_in.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.ui.auth.api.AuthApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Model
import kz.bloom.ui.auth.store.AuthStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class SignInComponentImpl(
    componentContext: ComponentContext,
    private val onCreateAccount:() -> Unit,
    private val onNavigateBack:() -> Unit,
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
            password = ""
        )
    )

    private val store = instanceKeeper.getStore {
        AuthStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    override val model: Value<Model> = _model

    override fun fillEmail(email: String) {
        _model.update { it.copy(email = email) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun enterAccount() {
        store.accept(intent = AuthStore.Intent.EnterAccount(model = _model.value))
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
}