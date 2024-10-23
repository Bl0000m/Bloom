package kz.bloom.ui.auth.sign_up.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.Model
import org.koin.core.component.KoinComponent

class SignUpComponentImpl(
    componentContext: ComponentContext,
    private val onLoginPage:() -> Unit,
) : SignUpComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val _model: MutableValue<Model> = MutableValue(
        initialValue = Model(
            name = "",
            email = "",
            phoneNumber = "",
            password = "",
            passwordConfirm = "",
            userAgreesToReceiveInfo = false
        )
    )

    override val model: Value<Model> = _model

    override fun fillName(name: String) {
        _model.update { it.copy(name = name) }
    }

    override fun fillMail(email: String) {
       _model.update { it.copy(email = email) }
    }

    override fun fillPhone(phoneNumber: String) {
        _model.update { it.copy(phoneNumber = phoneNumber) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun fillPasswordConfirm(rePassword: String) {
        _model.update { it.copy(passwordConfirm = rePassword) }
    }

    override fun userDoesAgreeToReceiveInfo(tick: Boolean) {
        _model.update { it.copy(userAgreesToReceiveInfo = tick) }
    }

    override fun createAccount() {
        onLoginPage()
    }
}