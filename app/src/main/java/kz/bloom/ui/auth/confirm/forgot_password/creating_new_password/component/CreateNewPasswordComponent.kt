package kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.ErrorBody

interface CreateNewPasswordComponent {
    public data class Model(
        public val password: String,
        public val confirmPassword: String,
        val passwordErrorOccurred: ErrorBody,
        val confirmPasswordErrorOccurred: ErrorBody,
    )

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    public val model: Value<Model>

    public fun fillPassword(password: String)

    public fun fillConfirmPassword(confirmPassword: String)

    public fun createNewPass()

    public fun onNavigateBack()

    public fun onConfirmPasswordFocusLost()

    public fun onPasswordFocusLost()
}