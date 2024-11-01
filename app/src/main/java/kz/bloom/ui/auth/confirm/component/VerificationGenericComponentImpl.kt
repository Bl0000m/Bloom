package kz.bloom.ui.auth.confirm.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent

import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent.Model
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent.VerificationKind
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind


class VerificationGenericComponentImpl(
    componentContext: ComponentContext,
    kind: VerificationKind,
    private val openOutcomePage:(kind: OutcomeKind) -> Unit,
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

    override fun confirmEmail() {
        openOutcomePage(OutcomeKind.Welcome)
    }

    override fun fillInEmailToRestorePass() {
        _model.update { it.copy(kind = VerificationKind.ForgotPassFillCode) }
    }

    override fun fillInCodeToRestorePass() {
        _model.update { it.copy(kind = VerificationKind.CreateNewPass) }
    }

    override fun createNewPass() {
        openOutcomePage(OutcomeKind.RestoreSuccess)
    }

    override fun onNavigateBack() {
        onBack()
    }
}