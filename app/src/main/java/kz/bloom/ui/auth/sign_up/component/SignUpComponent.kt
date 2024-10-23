package kz.bloom.ui.auth.sign_up.component

import com.arkivanov.decompose.value.Value

interface SignUpComponent {

    val model: Value<Model>

    data class Model(
        val name: String,
        val email: String,
        val phoneNumber: String,
        val password: String,
        val passwordConfirm: String,
        val userAgreesToReceiveInfo: Boolean
    )

    fun fillName(name: String)

    fun fillMail(email: String)

    fun fillPhone(phoneNumber: String)

    fun fillPassword(password: String)

    fun fillPasswordConfirm(rePassword: String)

    fun userDoesAgreeToReceiveInfo(tick: Boolean)

    fun createAccount()

}