package kz.bloom.ui.auth.sign_in.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_in.store.SignInStore.Intent
import kz.bloom.ui.auth.sign_in.store.SignInStore.State
import kz.bloom.ui.auth.sign_in.store.SignInStore.Label

interface SignInStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val accountEntered: Boolean
    ) : JvmSerializable

    sealed interface Label : JvmSerializable {
        data class ReceivedError(val message: String) : Label
    }

    sealed interface Intent : JvmSerializable {
        data class EnterAccount(val model: SignInComponent.Model) : Intent
    }
}