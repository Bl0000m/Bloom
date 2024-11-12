package kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component

import com.arkivanov.decompose.value.Value

interface CheckEmailFillCodeComponent {
    public data class Model(
        public val code: String,
        public val codeCanBeRequestedAgain: Boolean,
        )

    public val model: Value<Model>

    public fun fillEditText(value: String)

    public fun sendCode()

    public fun onNavigateBack()
}