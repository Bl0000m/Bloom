package kz.bloom.ui.auth.confirm.confirm_email.component

import com.arkivanov.decompose.value.Value

interface ConfirmEmailComponent {
    public data class Model(
        public val code: String,
        public val codeCanBeRequestedAgain: Boolean
        )

    public val model: Value<Model>

    public fun fillEditText(value: String)

    public fun confirmEmail()

    public fun onNavigateBack()

    public fun codeCanBeRequestedAgain(canBe: Boolean)

    public fun sendCodeAgain()
}