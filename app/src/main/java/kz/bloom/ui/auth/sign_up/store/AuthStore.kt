package kz.bloom.ui.auth.sign_up.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

import kz.bloom.ui.auth.sign_up.store.AuthStore.State
import kz.bloom.ui.auth.sign_up.store.AuthStore.Intent

interface AuthStore : Store<Intent, State, Nothing> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val accountCreated: Boolean,
        val accountEntered: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class CreateAccount(val model: SignUpComponent.Model) : Intent
        data class EnterAccount(val model: SignInComponent.Model) : Intent
    }
}