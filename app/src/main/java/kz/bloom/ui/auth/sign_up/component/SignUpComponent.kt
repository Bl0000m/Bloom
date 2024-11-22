package kz.bloom.ui.auth.sign_up.component

import androidx.compose.material3.SnackbarData
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kz.bloom.ui.auth.country_chooser.component.CountryModel

interface SignUpComponent {

    val model: Value<Model>

    data class Model(
        val name: String,
        val email: String,
        val phoneNumber: String,
        val password: String,
        val passwordConfirm: String,
        val userAgreesToReceiveInfo: Boolean,
        val selectedCountry: CountryModel,
        val nameErrorOccurred: ErrorBody,
        val emailErrorOccurred: ErrorBody,
        val phoneNumberErrorOccurred: ErrorBody,
        val passwordErrorOccurred: ErrorBody,
        val confirmPasswordErrorOccurred: ErrorBody,
        val snackBarErrorMessage: String
    )

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    fun fillName(name: String)

    fun onScreenReopened()

    fun fillMail(email: String)

    fun fillPhone(phoneNumber: String)

    fun fillPassword(password: String)

    fun fillPasswordConfirm(rePassword: String)

    fun userDoesAgreeToReceiveInfo(tick: Boolean)

    fun clearPhone()

    fun createAccount()

    fun navigateBack()

    fun openCountryChooser()

    fun updateSelectedCountry(country: CountryModel)

    fun onNameFocusLost()

    fun onEmailFocusLost()

    fun onPhoneFocusLost()

    fun onPasswordFocusLost()

    fun onConfirmPasswordFocusLost()

    data class ErrorBody(
        val errorOccurred: Boolean,
        val errorText: String,
        val wasFocusedBefore: Boolean
    )
}