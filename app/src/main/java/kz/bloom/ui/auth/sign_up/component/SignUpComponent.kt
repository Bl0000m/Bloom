package kz.bloom.ui.auth.sign_up.component

import androidx.compose.material3.SnackbarData
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.country_chooser.component.CountryModel

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

    fun fillName(name: String)

    fun fillMail(email: String)

    fun fillPhone(phoneNumber: String)

    fun fillPassword(password: String)

    fun fillPasswordConfirm(rePassword: String)

    fun userDoesAgreeToReceiveInfo(tick: Boolean)

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