package kz.bloom.ui.auth.sign_up.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.auth.sign_up.store.SignUpStore.Label
import kz.bloom.ui.auth.sign_up.store.SignUpStore.State
import kz.bloom.ui.auth.sign_up.store.SignUpStore.Intent

interface SignUpStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val accountCreated: Boolean
    ) : JvmSerializable

    sealed interface Label : JvmSerializable {
        data class ReceivedError(val message: String) : Label
    }

    sealed interface Intent : JvmSerializable {
        data class CreateAccount(val model: SignUpComponent.Model) : Intent
    }
}