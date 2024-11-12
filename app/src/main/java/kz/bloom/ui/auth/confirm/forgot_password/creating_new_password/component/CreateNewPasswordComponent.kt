package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component

import com.arkivanov.decompose.value.Value

interface CreateNewPasswordComponent {
    public data class Model(
        public val password: String,
        public val confirmPassword: String
    )


    public val model: Value<Model>

    public fun fillPassword(password: String)

    public fun fillConfirmPassword(confirmPassword: String)

    public fun createNewPass()

    public fun onNavigateBack()
}