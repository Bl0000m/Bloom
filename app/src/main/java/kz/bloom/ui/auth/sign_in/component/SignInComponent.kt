package kz.bloom.ui.auth.sign_in.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

public interface SignInComponent {

    public val model: Value<Model>

    public data class Model(
        val email: String,
        val password: String
    )

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    public fun fillEmail(email: String)

    public fun fillPassword(password: String)

    public fun enterAccount()

    public fun createAccount()

    public fun navigateBack()

    public fun forgotPassword()
}