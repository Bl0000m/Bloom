package kz.bloom.ui.auth.pass_code.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kz.bloom.ui.auth.api.AuthApi
import org.koin.core.component.KoinComponent

import kz.bloom.ui.auth.pass_code.component.PassCodeComponent.Model
import kz.bloom.ui.auth.pass_code.store.PassCodeStore
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Intent
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Label
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent.Event

import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

public class PassComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
) : PassCodeComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val authApi by inject<AuthApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val storeFactory by inject<StoreFactory>()

    private val _model = MutableValue(
        initialValue = Model(
            pinCode = "",
            pinLength = 0,
            confirmPinCode = "",
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

    override fun updatePinCode(pinCode: String) {
        sharedPreferences.pincode = pinCode
        onCloseClick()
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
        val maxLength = 8

        if (currentPin.length < maxLength) {
            val newPin = currentPin + number
            _model.update { it.copy(pinCode = newPin, pinLength = newPin.length) }

            if (newPin.length == maxLength) {
                saveOrVerifyPin(newPin)
            }
        }
    }

    override fun onDeleteClick() {
        val currentPin = _model.value.pinCode
        if (currentPin.isNotEmpty()) {
            val updatedPin = currentPin.dropLast(1)
            _model.update { it.copy(pinCode = updatedPin, pinLength = updatedPin.length) }
        }
    }

    private fun saveOrVerifyPin(enteredPin: String) {
        if (enteredPin.length == 8) {
            val firstHalf = enteredPin.substring(0, 4)
            val secondHalf = enteredPin.substring(4, 8)

            if (firstHalf == secondHalf) {
                updatePinCode(firstHalf)
                _model.update { it.copy(pinCode = "") }
            } else {
                _model.update { it.copy(pinCode = "", pinCodeMissMatch = true) }
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