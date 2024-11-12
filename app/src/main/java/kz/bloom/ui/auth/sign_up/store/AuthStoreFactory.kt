package kz.bloom.ui.auth.sign_up.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.sign_up.store.AuthStore.Intent
import kz.bloom.ui.auth.sign_up.store.AuthStore.State
import org.koin.androidx.compose.get
import kotlin.coroutines.CoroutineContext


private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data object AccountCreated : Message
    data object AccountEntered : Message
    data object ConfirmCodeSent : Message
    data object ConfirmCodeReceived : Message
    data object NewPassCreated : Message
}

private sealed interface Action : JvmSerializable {

}

internal fun AuthStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory
) : AuthStore =
    object : AuthStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Action, Message, State, Nothing>(
        name = "AuthStore",
        initialState = State(
            isError = false,
            accountCreated = false,
            accountEntered = false,
            isLoading = false,
            confirmCodeReceived = false,
            confirmCodeSent = false,
            newPassCreated = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = true, isLoading = false)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
                is Message.AccountCreated -> copy(accountCreated = true, isError = false, isLoading = false)
                is Message.AccountEntered -> copy(accountEntered = true, isLoading = false)
                is Message.ConfirmCodeReceived -> copy(confirmCodeReceived = true)
                is Message.ConfirmCodeSent -> copy(confirmCodeSent = true)
                is Message.NewPassCreated -> copy(newPassCreated = true)
            }
        },
        bootstrapper = SimpleBootstrapper(),
        executorFactory = {
            ExecutorImpl(
                mainContext = mainContext,
                ioContext = ioContext,
                authApi = authApi
            )
        }
    ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val authApi: AuthApi
) : CoroutineExecutor<Intent, Action, State, Message, Nothing> (
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

                    } catch (exception: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }

            is Intent.CreateNewPass -> {
                scope.launch {
                    try {
                        val createNewPass = withContext(context = ioContext) {
                            authApi.createNewPass(email = intent.email, password = intent.password, confirmPassword = intent.confirmPassword)
                        }
                        if (createNewPass.status.value == 200) {
                            dispatch(message = Message.NewPassCreated)
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            is Intent.EnterAccount -> {
                scope.launch {
                    try {
                        dispatch(
                            message = Message.LoadingChanged(
                                isLoading = true
                            )
                        )
                        val enterAccount = withContext(context = ioContext) {
                            authApi.enterAccount(model = intent.model)
                        }

                    } catch (exception: Exception) {
                        Log.d("beholdError", exception.toString())
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }

            is Intent.ReceiveConfirmCode -> {
                scope.launch {
                    try {
                        delay(timeMillis = 1000)
                        val getConfirmCode = withContext(context = ioContext) {
                            authApi.getConfirmCode(email = intent.email)
                        }

                        delay(timeMillis = 500)
                        if (getConfirmCode.status.value == 200) {
                            dispatch(message = Message.ConfirmCodeReceived)
                        }
                    } catch (exception: Exception) {
                        Log.d("beholdError", exception.toString())
                    }
                }
            }

            is Intent.ValidateReceivedCode -> {
                scope.launch {
                    try {
                        val sendConfirmCode = withContext(context = ioContext) {
                            authApi.sendConfirmCode(email = intent.email, code = intent.code)
                        }

                        if (sendConfirmCode.status.value == 200) {
                            dispatch(message = Message.ConfirmCodeSent)
                        }
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    override fun executeAction(action: Action, getState: () -> State) {

    }
}