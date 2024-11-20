package kz.bloom.ui.auth.sign_up.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.ErrorBody
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.Model
import kz.bloom.ui.auth.country_chooser.component.CountryModel
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.Event
import kz.bloom.ui.auth.sign_up.store.SignUpStore
import kz.bloom.ui.auth.sign_up.store.SignUpStore.Label
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext


class SignUpComponentImpl(
    componentContext: ComponentContext,
    selectedCountry: CountryModel? = null,
    private val onCreateAccount: (email: String) -> Unit,
    private val onError: (OutcomeKind) -> Unit,
    private val onNavigateBack: () -> Unit,
    private val onOpenCountryChooser: () -> Unit
) : SignUpComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()

    private val store = instanceKeeper.getStore {
        SignUpStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }
    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()

    private val scope = coroutineScope()

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )

    private val startingCountryModel = CountryModel(
        code = "KZ",
        name = "Казахстан",
        dialCode = "+7",
        flagEmoji = "\uD83C\uDDF0\uD83C\uDDFF",
        phoneNumberLength = 10
    )

    private val _model: MutableValue<Model> = MutableValue(
        initialValue = Model(
            name = "",
            email = "",
            phoneNumber = "",
            password = "",
            passwordConfirm = "",
            userAgreesToReceiveInfo = false,
            selectedCountry = selectedCountry ?: startingCountryModel,
            nameErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            emailErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            phoneNumberErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            passwordErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            confirmPasswordErrorOccurred = ErrorBody(
                errorOccurred = false,
                errorText = "",
                wasFocusedBefore = false
            ),
            snackBarErrorMessage = ""
        )
    )

    override val model: Value<Model> = _model

    override fun fillName(name: String) {
        _model.update { it.copy(nameErrorOccurred = ErrorBody(errorOccurred = false, errorText = "", wasFocusedBefore = true)) }
        _model.update { it.copy(name = name) }
    }

    override fun fillMail(email: String) {
        _model.update { it.copy(emailErrorOccurred = ErrorBody(errorOccurred = false, errorText = "", wasFocusedBefore = true)) }
        _model.update { it.copy(email = email) }
    }

    override fun fillPhone(phoneNumber: String) {
        _model.update { it.copy(phoneNumberErrorOccurred = ErrorBody(errorOccurred = false, errorText = "", wasFocusedBefore = true)) }
        _model.update { it.copy(phoneNumber = phoneNumber) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(passwordErrorOccurred = ErrorBody(errorOccurred = false, errorText = "", wasFocusedBefore = true)) }
        _model.update { it.copy(password = password) }
    }

    override fun fillPasswordConfirm(rePassword: String) {
        _model.update { it.copy(confirmPasswordErrorOccurred = ErrorBody(errorOccurred = false, errorText = "", wasFocusedBefore = true)) }
        _model.update { it.copy(passwordConfirm = rePassword) }
    }

    override fun userDoesAgreeToReceiveInfo(tick: Boolean) {
        _model.update { it.copy(userAgreesToReceiveInfo = tick) }
    }

    override fun clearPhone() {
        _model.update { it.copy(phoneNumber = "") }
    }

    override fun createAccount() {
        if(validateAllFields()) {
            store.accept(
                intent = SignUpStore.Intent.CreateAccount(
                    model = _model.value.copy(
                        phoneNumber = _model.value.selectedCountry.dialCode + _model.value.phoneNumber
                    )
                )
            )
            scope.launch {
                delay(timeMillis = 500L)
                store.states.subscribe { state ->
                    if (state.accountCreated && !state.isLoading) {
                        onCreateAccount(_model.value.email)
                        sharedPreferences.password = _model.value.password
                        sharedPreferences.username = _model.value.email
                    } else if (!state.accountCreated && !state.isLoading) {
                        onError(OutcomeKind.Error)
                    }
                }
            }
        }
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    override fun openCountryChooser() {
        onOpenCountryChooser()
    }

    override fun updateSelectedCountry(country: CountryModel) {
        _model.update { it.copy(selectedCountry = country) }
    }

    override fun onNameFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.nameErrorOccurred,
            validateField = { validateName(_model.value.name) },
            didErrorOccur = { validateName(_model.value.name) != "" },
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(nameErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onEmailFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.emailErrorOccurred,
            validateField = { validateEmail(_model.value.email) },
            didErrorOccur = { validateEmail(_model.value.email) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(emailErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onPhoneFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.phoneNumberErrorOccurred,
            validateField = { validatePhoneNumber(_model.value.phoneNumber) },
            didErrorOccur = { validatePhoneNumber(_model.value.phoneNumber) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(phoneNumberErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onPasswordFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.passwordErrorOccurred,
            validateField = { validatePassword(_model.value.password) },
            didErrorOccur = { validatePassword(_model.value.password) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(passwordErrorOccurred = updatedErrorBody) }
            }
        )
    }

    override fun onConfirmPasswordFocusLost() {
        updateFieldErrorOnSecondFocusLost(
            errorBody = _model.value.confirmPasswordErrorOccurred,
            validateField = { validateConfirmPassword(_model.value.password, _model.value.passwordConfirm) },
            didErrorOccur = { validateConfirmPassword(_model.value.password, _model.value.passwordConfirm) != ""},
            updateModel = { updatedErrorBody ->
                _model.update { it.copy(confirmPasswordErrorOccurred = updatedErrorBody) }
            }
        )
    }
    private fun validateAllFields(): Boolean {
        var isValid = true

        _model.update { currentModel ->
            val updatedModel = currentModel.copy(
                nameErrorOccurred = updateFieldError(
                    currentModel.nameErrorOccurred,
                    { validateName(currentModel.name) }
                ),
                emailErrorOccurred = updateFieldError(
                    currentModel.emailErrorOccurred,
                    { validateEmail(currentModel.email) }
                ),
                phoneNumberErrorOccurred = updateFieldError(
                    currentModel.phoneNumberErrorOccurred,
                    { validatePhoneNumber(currentModel.phoneNumber) }
                ),
                passwordErrorOccurred = updateFieldError(
                    currentModel.passwordErrorOccurred,
                    { validatePassword(currentModel.password) }
                ),
                confirmPasswordErrorOccurred = updateFieldError(
                    currentModel.confirmPasswordErrorOccurred,
                    { validateConfirmPassword(currentModel.password, currentModel.passwordConfirm) }
                )
            )
            isValid = isValid && !(
                    updatedModel.nameErrorOccurred.errorOccurred ||
                            updatedModel.emailErrorOccurred.errorOccurred ||
                            updatedModel.phoneNumberErrorOccurred.errorOccurred ||
                            updatedModel.passwordErrorOccurred.errorOccurred ||
                            updatedModel.confirmPasswordErrorOccurred.errorOccurred
                    )

            updatedModel
        }

        return isValid
    }
}

fun validateEmail(email: String): String {
    if (email.isEmpty()) {
        return "Заполните поле e-mail."
    }

    val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}".toRegex()
    if (!email.matches(emailRegex)) {
        return "Укажите корректный e-mail."
    }

    return ""
}

fun validatePassword(password: String): String {
    if (password.isEmpty()) {
        return "Заполните поле."
    }

    if (password.length < 8) {
        return "Минимум 8 символов."
    }

    val digitRegex = ".*[0-9]+.*".toRegex()
    val letterRegex = ".*[A-Za-z]+.*".toRegex()

    if (!password.contains(digitRegex) || !password.contains(letterRegex)) {
        return "Используйте буквы и цифры."
    }

    val uppercaseLetterRegex = ".*[A-Z]+.*".toRegex()
    val lowercaseLetterRegex = ".*[a-z]+.*".toRegex()
    val specialCharacterRegex = ".*[!@#$&*]+.*".toRegex()

    if (!password.contains(uppercaseLetterRegex) ||
        !password.contains(lowercaseLetterRegex) ||
        !password.contains(specialCharacterRegex)) {
        return "Добавьте заглавные, строчные буквы, цифру и символ."
    }

    return ""
}

fun validateConfirmPassword(password: String, confirmPassword: String): String {
    if (confirmPassword.isEmpty()) {
        return "Заполните поле."
    }
    if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
        if (password == confirmPassword) return ""
    }
    if (password != confirmPassword) {
        return "Проверьте пароли."
    }
    return ""
}

fun validatePhoneNumber(phoneNumber: String): String {
    val phoneNumberRegex = "^\\d{10,15}$".toRegex()
    if (phoneNumber.isEmpty()) {
        return "Заполните поле."
    }
    return if (!phoneNumber.matches(phoneNumberRegex)) {
        "Некорректный формат номера телефона"
    } else ""
}

fun validateName(name: String): String {
    val nameRegex = "^[A-Za-zА-Яа-я]+$".toRegex()

    if (!name.matches(nameRegex) && name.isNotEmpty()) {
        return "Используйте только буквы."
    }
    if (name.length < 3) {
        return "Минимум 3 символа."
    }
    if (name.isEmpty()) {
        return "Заполните поле."
    }

    return ""
}

private fun updateFieldErrorOnSecondFocusLost(
    errorBody: ErrorBody,
    validateField: () -> String,
    didErrorOccur: () -> Boolean,
    updateModel: (ErrorBody) -> Unit
) {
    if (errorBody.wasFocusedBefore) {
        updateModel(
            errorBody.copy(
                errorText = validateField(),
                errorOccurred = didErrorOccur()
            )
        )
    } else {
        updateModel(
            errorBody.copy(
                wasFocusedBefore = true
            )
        )
    }
}

private fun Flow<Label>.toEvents(): Flow<Event> = map { label ->
    when(label) {
        is Label.ReceivedError -> {
            Event.DisplaySnackBar(errorMessage = label.message)
        }
    }
}

private fun updateFieldError(
    errorBody: ErrorBody,
    validateField: () -> String
): ErrorBody {
    val errorText = validateField()
    return errorBody.copy(
        errorOccurred = errorText.isNotEmpty(),
        errorText = errorText
    )
}