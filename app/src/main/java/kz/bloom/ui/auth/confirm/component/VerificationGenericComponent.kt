package kz.bloom.ui.auth.confirm.component

import com.arkivanov.decompose.value.Value

interface VerificationGenericComponent {

    public data class Model(
        public val editTextValue: String,
        public val kind: VerificationKind
    )

    sealed interface VerificationKind {
        data object ConfirmEmail : VerificationKind
        data object ForgotPassFillEmail : VerificationKind
        data object ForgotPassFillCode: VerificationKind
        data object CreateNewPass: VerificationKind
    }

    public val model: Value<Model>

    public fun fillEditText(value: String)

    public fun confirmEmail()

    public fun fillInEmailToRestorePass()

    public fun fillInCodeToRestorePass()

    public fun createNewPass()

    public fun onNavigateBack()
}