package kz.bloom.ui.auth.sign_in.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Model

class SignInComponentImpl(
    componentContext: ComponentContext
): SignInComponent,
   KoinComponent,
   ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            email = "",
            password = ""
        )
    )

    override val model: Value<Model> = _model

    override fun fillEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun fillPassword(password: String) {
        TODO("Not yet implemented")
    }

    override fun enterAccount() {

    }

    override fun createAccount() {

    }
}