package kz.bloom.ui.auth.sign_in.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent.Model

class SignInComponentImpl(
    componentContext: ComponentContext,
    private val onCreateAccount:() -> Unit,
    private val onNavigateBack:() -> Unit,
    private val onForgotPassword:() -> Unit
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
        _model.update { it.copy(email = email) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun enterAccount() {

    }

    override fun createAccount() {
        onCreateAccount()
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    override fun forgotPassword() {
        onForgotPassword()
    }
}