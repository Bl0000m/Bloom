package kz.bloom.ui.auth.confirm_email.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent

import kz.bloom.ui.auth.confirm_email.component.VerificationGenericComponent.Model
import kz.bloom.ui.auth.confirm_email.component.VerificationGenericComponent.VerificationKind

class VerificationGenericComponentImpl(
    componentContext: ComponentContext,
    kind: VerificationKind,
    private val openOutcomePage:() -> Unit,
    private val onBack:() -> Unit
) : VerificationGenericComponent,
    KoinComponent,
    ComponentContext by componentContext {

        private val _model = MutableValue(
            initialValue = Model(
                editTextValue = "",
                kind = kind
            )
        )
    override val model: Value<Model> = _model

    override fun fillEditText(value: String) {
        _model.update { it.copy(editTextValue = value) }
    }

    override fun confirmEmail(code: String) {
        TODO("Not yet implemented")
    }

    override fun fillInEmailToRestorePass(email: String) {
        TODO("Not yet implemented")
    }

    override fun fillInCodeToRestorePass(codeForRestore: String) {
        TODO("Not yet implemented")
    }

    override fun createNewPass(newPass: String) {
        TODO("Not yet implemented")
    }

    override fun onNavigateBack() {
        onBack()
    }
}