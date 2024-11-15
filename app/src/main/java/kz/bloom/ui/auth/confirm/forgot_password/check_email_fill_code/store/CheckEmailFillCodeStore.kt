package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.State
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Label
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.store.CheckEmailFillCodeStore.Intent

interface CheckEmailFillCodeStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val confirmCodeReceived: Boolean,
        val confirmCodeSent: Boolean,
        val serverIsNotResponding: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class ReceiveConfirmCode(val email: String) : Intent
        data class ValidateReceivedCode(val email: String, val code: String) : Intent
    }
    sealed interface Label : JvmSerializable {
        data class ReceivedError(val message: String) : Label
    }
}