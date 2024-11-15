package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store

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
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Label
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.State

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data object NewPassCreated : Message
}

private sealed interface Action : JvmSerializable {

}

internal fun CreateNewPasswordStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    sharedPreferences: SharedPreferencesSetting,
    storeFactory: StoreFactory,
    authApi: AuthApi
) : CreateNewPasswordStore =
    object : CreateNewPasswordStore, Store<Intent, State, Label>
    by storeFactory.create<Intent,Action,Message,State,Label>(
        name = "CreateNewPasswordStore",
        initialState = State(
            isError = false,
            isLoading = false,
            newPassCreated = false
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> copy(isError = false)
                is Message.NewPassCreated -> copy(newPassCreated = true)
                is Message.LoadingChanged -> copy(isLoading = message.isLoading)
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
            is Intent.CreateNewPass -> {
                scope.launch {
                    try {
                        val createNewPass = withContext(context = ioContext) {
                            authApi.createNewPass(email = intent.email, password = intent.password, confirmPassword = intent.confirmPassword)
                        }
                        if (createNewPass.status.value == 200) {
                            dispatch(message = Message.NewPassCreated)
                        }
                    } catch (exception: ApiException) {
                        publish(label = Label.ErrorReceived(message = exception.error.message))
                    }
                }
            }
        }
    }
}