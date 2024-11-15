package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.application.module.ApiException
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.State
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Label
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data object ConfirmCodeSent : Message
    data object ConfirmCodeReceived : Message
    data object ServerNotResponding : Message
}

private sealed interface Action : JvmSerializable {

}

internal fun CheckEmailFillCodeStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting
) : CheckEmailFillCodeStore =
    object : CheckEmailFillCodeStore, Store<Intent, State, Label>
    by storeFactory.create<Intent, Action, Message, State, Label>    (
        name = "CheckEmailFillCodeStore",
        initialState = State(
            isLoading = false,
            isError = false,
            confirmCodeReceived = false,
            confirmCodeSent = false,
            serverIsNotResponding = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ConfirmCodeSent -> copy(confirmCodeSent = true)
                is Message.ConfirmCodeReceived -> copy(confirmCodeReceived = true)
                is Message.ServerNotResponding -> copy(serverIsNotResponding = true)
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
                            authApi.getConfirmCode(
                                email = intent.email
                            )
                        }
                        if (getConfirmCode.status.value == 200) {
                            dispatch(message = Message.ConfirmCodeReceived)
                        } else if (getConfirmCode.status.value == 502) {
                            dispatch(message = Message.ServerNotResponding)
                        }
                    } catch(e: ApiException) {
                        publish(label = Label.ReceivedError(message = e.error.message))
                    }
                }
            }
            is Intent.ValidateReceivedCode -> {
                scope.launch {
                    try {
                        val sendConfirmCode = withContext(context = ioContext) {
                            authApi.sendConfirmCode(
                                email = intent.email,
                                code = intent.code
                            )
                        }

                        if (sendConfirmCode.status.value == 200) {
                            dispatch(message = Message.ConfirmCodeSent)
                        }
                    } catch (e: ApiException) {
                        publish(label = Label.ReceivedError(message = e.error.message))
                    }
                }
            }
        }
    }
}