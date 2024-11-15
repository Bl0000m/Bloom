package kz.bloom.ui.auth.sign_up.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.application.module.ApiException
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

import kz.bloom.ui.auth.sign_up.store.SignUpStore.State
import kz.bloom.ui.auth.sign_up.store.SignUpStore.Label
import kz.bloom.ui.auth.sign_up.store.SignUpStore.Intent


private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred: Message
    data object AccountCreated: Message
}

private sealed interface Action : JvmSerializable {

}

internal fun SignUpStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting
) : SignUpStore =
    object : SignUpStore, Store<Intent, State, Label>
    by storeFactory.create<Intent,Action,Message,State,Label>(
        name = "SignUpStore",
        initialState = State(
            isLoading = false,
            isError = false,
            accountCreated = false
        ),
        reducer = { message ->
            when(message) {
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
                is Message.ErrorOccurred -> copy(isError = true)
                is Message.AccountCreated -> copy(accountCreated = true)
            }
        },
        bootstrapper = SimpleBootstrapper(),
        executorFactory = {
            ExecutorImpl(
                mainContext = mainContext,
                ioContext = ioContext,
                authApi = authApi,
                sharedPreferences = sharedPreferences
            )
        }
    ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val sharedPreferences: SharedPreferencesSetting,
    private val ioContext: CoroutineContext,
    private val authApi: AuthApi
) : CoroutineExecutor<Intent, Action, State, Message, Label>(
    mainContext = mainContext
) {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when(intent) {
            is Intent.CreateAccount -> {
                scope.launch {
                    try {
                        dispatch(
                            message = Message.LoadingChanged(
                                isLoading = true
                            )
                        )
                        val createAccount = withContext(context = ioContext) {
                            authApi.createAccount(model = intent.model)
                        }
                        if (createAccount.status.value == 200) {
                            dispatch(message = Message.AccountCreated)
                        }
                        dispatch(
                            message = Message.LoadingChanged(
                                isLoading = false
                            )
                        )
                    }
                    catch (exception: ApiException) {
                        publish(label = Label.ReceivedError(message = exception.error.message))
                    }
                }
            }
        }
    }
}