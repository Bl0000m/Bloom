package kz.bloom.ui.auth.confirm.confirm_email.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore.Intent
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore.Label
import kz.bloom.ui.auth.confirm.confirm_email.store.ConfirmEmailStore.State

interface ConfirmEmailStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val confirmCodeReceived: Boolean,
        val confirmCodeSent: Boolean,
        val serverIsNotResponding: Boolean
    ) : JvmSerializable

    sealed interface Intent: JvmSerializable {
        data class ReceiveConfirmCode(val email :String) : Intent
        data class ValidateReceivedCode(val email: String, val code: String) : Intent
    }

    sealed interface Label : JvmSerializable {
        data class ErrorReceived(val message: String): Label
    }
}