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
        val accountEntered: Boolean,
        val confirmCodeReceived: Boolean,
        val confirmCodeSent: Boolean,
        val newPassCreated: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class CreateAccount(val model: SignUpComponent.Model) : Intent
        data class EnterAccount(val model: SignInComponent.Model) : Intent
        data class ReceiveConfirmCode(val  email: String) : Intent
        data class ValidateReceivedCode(val email: String, val code: String) : Intent
        data class CreateNewPass(val email: String, val password: String, val confirmPassword: String) : Intent
    }
}