package kz.bloom.ui.auth.confirm.forgot_password.fill_email.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Intent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.State
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.store.FillEmailStore.Label


interface FillEmailStore : Store<Intent, State, Label> {
    data class State (
        val isError: Boolean,
        val isLoading: Boolean,
        val confirmCodeReceived: Boolean,
        val serverIsNotResponding: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class ReceiveConfirmCode(val email: String) : Intent
    }

    sealed interface Label : JvmSerializable {
        data class ErrorReceived(val message: String) : Label
    }
}