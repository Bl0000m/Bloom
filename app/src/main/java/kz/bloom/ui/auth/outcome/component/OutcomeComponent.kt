package kz.bloom.ui.auth.outcome.component

import com.arkivanov.decompose.value.Value

interface OutcomeComponent {
    data class Model(
        public val outcomeKind: OutcomeKind
    )

    sealed interface OutcomeKind{
        data object Welcome: OutcomeKind
        data object Error: OutcomeKind
        data object RestoreSuccess: OutcomeKind
    }

    public val model: Value<Model>

    public fun continuePressed()

    public fun navigateBack()
}