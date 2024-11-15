package kz.bloom.ui.auth.confirm.forgot_password.fill_email.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.application.module.ApiException
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.State
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Label

import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data object ConfirmCodeReceived : Message
    data object ServerIsNotResponding : Message
}

private sealed interface Action : JvmSerializable {

}

internal fun FillEmailStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting
) : FillEmailStore =
    object : FillEmailStore, Store<Intent, State, Label>
    by storeFactory.create<Intent, Action, Message, State, Label>(
        name = "AuthStore",
        initialState = State(
            isError = false,
            isLoading = false,
            confirmCodeReceived = false,
            serverIsNotResponding = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = true, isLoading = false)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
                is Message.ConfirmCodeReceived -> copy(confirmCodeReceived = true)
                is Message.ServerIsNotResponding -> copy(serverIsNotResponding = true)
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
            is Intent.ReceiveConfirmCode -> {
                scope.launch {
                    try {
                        val getConfirmCode = withContext(context = ioContext) {
                            authApi.getConfirmCode(email = intent.email)
                        }

                        if (getConfirmCode.status.value == 200) {
                            dispatch(message = Message.ConfirmCodeReceived)
                        } else if (getConfirmCode.status.value == 500 && getConfirmCode.status.value == 502) {
                            dispatch(message = Message.ServerIsNotResponding)
                        }

                    } catch (exception: ApiException) {
                        publish(label = Label.ErrorReceived(message = exception.error.message))
                    }
                }
            }
        }
    }
}