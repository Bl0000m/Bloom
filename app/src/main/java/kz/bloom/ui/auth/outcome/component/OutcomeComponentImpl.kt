package kz.bloom.ui.auth.outcome.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.Model
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind

class OutcomeComponentImpl(
    componentContext: ComponentContext,
    outcomeKind: OutcomeKind,
    private val onOpenPass:() -> Unit,
    private val onOpenSignIn:() -> Unit
) : OutcomeComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            outcomeKind = outcomeKind
        )
    )

    override val model: Value<Model> = _model

    override fun continuePressed(outcomeKind: OutcomeKind) {
        when(outcomeKind) {
            is OutcomeKind.Welcome -> { onOpenPass() }
            is OutcomeKind.RestoreSuccess -> { onOpenSignIn() }
            is OutcomeKind.Error -> { onOpenSignIn() }
        }
    }
}