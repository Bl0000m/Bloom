package kz.bloom.ui.auth.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kz.bloom.ui.auth.component.AuthComponent.Model
import org.koin.core.component.KoinComponent

class AuthComponentImpl(
    componentContext: ComponentContext
) : AuthComponent,
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
        TODO("Not yet implemented")
    }

    override fun fillPhone(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun fillPassword(password: String) {
        TODO("Not yet implemented")
    }

    override fun fillPasswordConfirm(rePassword: String) {
        TODO("Not yet implemented")
    }

    override fun userDoesAgreeToReceiveInfo(tick: Boolean) {
        TODO("Not yet implemented")
    }

    override fun createAccount() {

    }
}