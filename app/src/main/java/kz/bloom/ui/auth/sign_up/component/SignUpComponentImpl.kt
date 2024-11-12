package kz.bloom.ui.auth.sign_up.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.outcome.component.OutcomeComponent.OutcomeKind
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.ErrorBody
import kz.bloom.ui.auth.sign_up.component.SignUpComponent.Model
import kz.bloom.ui.auth.store.AuthStore
import kz.bloom.ui.country_chooser.component.CountryModel
import kz.bloom.ui.ui_components.coroutineScope
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
    private val storeFactory by inject<StoreFactory>()

    private val scope = coroutineScope()

    private val store = instanceKeeper.getStore {
        AuthStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory
        )
    }

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
        _model.update { it.copy(name = name) }
    }

    override fun fillMail(email: String) {
        _model.update { it.copy(email = email) }
    }

    override fun fillPhone(phoneNumber: String) {
        _model.update { it.copy(phoneNumber = phoneNumber) }
    }

    override fun fillPassword(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun fillPasswordConfirm(rePassword: String) {
        _model.update { it.copy(passwordConfirm = rePassword) }
    }

    override fun userDoesAgreeToReceiveInfo(tick: Boolean) {
        _model.update { it.copy(userAgreesToReceiveInfo = tick) }
    }

    override fun createAccount() {
        store.accept(
            intent = AuthStore.Intent.CreateAccount(
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
                } else if (!state.accountCreated && !state.isLoading) {
                    onError(OutcomeKind.Error)
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
    return if (password == confirmPassword) ""
    else if (confirmPassword.isEmpty()) {
        return "Заполните поле."
    }
    else "Проверьте пароли."
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

    if (!name.matches(nameRegex)) {
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

fun updateFieldErrorOnSecondFocusLost(
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