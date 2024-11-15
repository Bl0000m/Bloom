package kz.bloom.ui.auth.pass_code.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.application.module.ApiException
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Intent
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Label
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.State
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext


private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data object ServerIsNotResponding : Message
    data object TokenRefreshed: Message
}

private sealed interface Action : JvmSerializable {

}

internal fun PassCodeStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    authApi: AuthApi,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting
) : PassCodeStore =
    object : PassCodeStore, Store<Intent, State, Label>
    by storeFactory.create<Intent, Action, Message, State, Label>(
        name = "AuthStore",
        initialState = State(
            isError = false,
            isLoading = false,
            tokenRefreshed = false,
            serverIsNotResponding = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = true, isLoading = false)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
                is Message.TokenRefreshed -> copy(tokenRefreshed = true)
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
            is Intent.RefreshAccessToken -> {
                scope.launch {
                    try {
                        val refreshedAccessToken = withContext(context = ioContext) {
                            authApi.refreshAccessToken(intent.refreshToken)
                        }
                        if (refreshedAccessToken.accessToken.isNotEmpty()) {
                            sharedPreferences.accessToken = refreshedAccessToken.accessToken
                            sharedPreferences.refreshToken = refreshedAccessToken.refreshToken
                            dispatch(message = Message.TokenRefreshed)
                        }
                    } catch (exception: ApiException) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}