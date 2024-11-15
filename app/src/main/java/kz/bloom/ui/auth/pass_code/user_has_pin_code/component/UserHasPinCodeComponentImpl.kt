package kz.bloom.ui.auth.pass_code.user_has_pin_code.component

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
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
import kz.bloom.ui.auth.pass_code.user_has_pin_code.component.UserHasPincodeComponent.Event
import kz.bloom.ui.auth.pass_code.user_has_pin_code.component.UserHasPincodeComponent.Model
import kz.bloom.ui.auth.pass_code.store.PassCodeStore
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Label
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

public class UserHasPinCodeComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val allowForward:() -> Unit
) : UserHasPincodeComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()
    private val scope = coroutineScope()

    private val _model = MutableValue(
        initialValue = Model(
            pinCode = "",
            pinLength = 0,
            pinCodeMissMatch = false
        )
    )

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()


    private val store = instanceKeeper.getStore {
        PassCodeStore(
            authApi = authApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    override val events: Flow<Event> = merge(
        store.labels.toEvents(),
        _events
    )

    override val model: Value<Model> = _model

    override fun fillPass(pinCode: String) {
        _model.update { it.copy(pinCode = pinCode) }
    }

    override fun onBackClick() {
        onNavigateBack()
    }

    override fun onCloseClick() {
        _model.update { it.copy(pinCode = "") }
        onNavigateBack()
    }

    override fun onNumberClick(number: Int) {
        val currentPin = _model.value.pinCode
        val maxLength = 4

        if (currentPin.length < maxLength) {
            val newPin = currentPin + number
            _model.update { it.copy(pinCode = newPin, pinLength = newPin.length) }

            if (newPin.length == maxLength) {
                checkIfPinMatch(newPin)
            }
        }
    }

    override fun onDeleteClick() {
        _model.update { it.copy(pinCodeMissMatch = false) }
        val currentPin = _model.value.pinCode
        if (currentPin.isNotEmpty()) {
            val updatedPin = currentPin.dropLast(1)
            _model.update { it.copy(pinCode = updatedPin, pinLength = updatedPin.length) }
        }
    }

    private fun checkIfPinMatch(enteredPin: String) {
        if (sharedPreferences.pincode == enteredPin) {
            store.accept(
                intent =
                PassCodeStore.Intent.RefreshAccessToken(
                    refreshToken = sharedPreferences.refreshToken.toString()
                )
            )
        } else {
            _model.update { it.copy(pinCodeMissMatch = true) }
        }
        store.states.subscribe { state ->
            if (state.tokenRefreshed) {
                allowForward()
            }
        }
    }
}

private fun Flow<Label>.toEvents(): Flow<Event> = map { label ->
    when(label) {
        is Label.ErrorReceived -> {
            Event.DisplaySnackBar(errorMessage = label.message)
        }
    }
}