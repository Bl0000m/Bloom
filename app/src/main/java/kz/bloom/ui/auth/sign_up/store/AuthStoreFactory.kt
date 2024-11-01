package kz.bloom.ui.auth.sign_up.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.sign_up.store.AuthStore.Intent
import kz.bloom.ui.auth.sign_up.store.AuthStore.State
import kotlin.coroutines.CoroutineContext


private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data object AccountCreated : Message
}

internal fun AuthStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory
) : AuthStore =
    object : AuthStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Nothing, Message, State, Nothing>(
        name = "AuthStore",
        initialState = State(
            isError = false,
            isSuccess = false,
            isLoading = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = true, isSuccess = false, isLoading = false)
                is Message.AccountCreated -> copy(isSuccess = true, isError = false, isLoading = false)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
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
) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing> (
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
                            Log.d("behold11","Account Created")
                            dispatch(message = Message.AccountCreated)
                        }

                    } catch (exception: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}