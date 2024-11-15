package kz.bloom.ui.auth.confirm.forgot_password.fill_email.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface FillEmailComponent {
    data class Model(
        val email: String
    )

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    public val model: Value<Model>

    public fun fillEmail(email: String)

    public fun continueAndGetCode()

    public fun onNavigateBack()
}