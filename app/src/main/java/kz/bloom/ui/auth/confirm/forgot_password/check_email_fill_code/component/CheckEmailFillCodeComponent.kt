package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface CheckEmailFillCodeComponent {
    public data class Model(
        public val code: String,
        public val codeCanBeRequestedAgain: Boolean,
        )

    public val model: Value<Model>

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    public fun fillEditText(value: String)

    public fun sendCode()

    public fun codeCanBeRequestedAgain(canBe: Boolean)

    public fun sendCodeAgain()

    public fun onNavigateBack()
}