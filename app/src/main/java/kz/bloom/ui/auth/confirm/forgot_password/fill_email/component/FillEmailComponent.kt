package kz.bloom.ui.auth.confirm.forgot_password.fill_email.component

import com.arkivanov.decompose.value.Value

interface FillEmailComponent {
    data class Model(
        val email: String
    )

    public val model: Value<Model>

    public fun fillEmail(email: String)

    public fun continueAndGetCode()

    public fun onNavigateBack()
}