package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.State
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.store.CreateNewPasswordStore.Label

interface CreateNewPasswordStore : Store<Intent, State, Label> {
    public data class State(
        val isError: Boolean,
        val isLoading: Boolean,
        val newPassCreated: Boolean
    ) : JvmSerializable

    public sealed interface Intent : JvmSerializable {
        data class CreateNewPass(
            val email: String,
            val password: String,
            val confirmPassword: String
        ) : Intent
    }
    public sealed interface Label : JvmSerializable {
        data class ErrorReceived(val message: String) : Label
    }
}