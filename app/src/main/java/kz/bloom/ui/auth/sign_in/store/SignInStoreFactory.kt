package kz.bloom.ui.auth.sign_in.store

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
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
import kz.bloom.ui.auth.sign_in.store.SignInStore.Label
import kz.bloom.ui.auth.sign_in.store.SignInStore.Intent
import kz.bloom.ui.auth.sign_in.store.SignInStore.State
import kz.bloom.ui.auth.sign_in.store.SignInStore

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred: Message
    data object AccountEntered: Message
}

private sealed interface Action: JvmSerializable {

}

internal fun SignInStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting
) : SignInStore =
    object : SignInStore, Store<Intent, State, Label>
    by storeFactory.create<Intent,Action,Message,State,Label>(
        name = "SignInStore",
        initialState = State(
            isLoading = false,
            isError = false,
            accountEntered = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = true)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
                is Message.AccountEntered -> copy(accountEntered = true)
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
) : CoroutineExecutor<Intent,Action,State,Message, Label>(
    mainContext = mainContext
) {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when(intent) {
            is Intent.EnterAccount -> {
                scope.launch {
                    try {
                        dispatch(
                            message = Message.LoadingChanged(
                                isLoading = true
                            )
                        )
                        val enteredAccount = withContext(context = ioContext) {
                            authApi.enterAccount(username = intent.model.email, password = intent.model.password)
                        }

                        if (enteredAccount.accessToken.isNotEmpty()) {
                            dispatch(message = Message.AccountEntered)
                            sharedPreferences.accessToken = enteredAccount.accessToken
                            sharedPreferences.refreshToken = enteredAccount.refreshToken
                        }

                    } catch (e: ApiException) {
                        publish(label = Label.ReceivedError(message = e.error.message))
                    }
                }
            }
        }
    }
}