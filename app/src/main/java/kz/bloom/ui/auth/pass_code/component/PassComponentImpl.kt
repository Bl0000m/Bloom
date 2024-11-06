package kz.bloom.ui.auth.pass_code.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent

import kz.bloom.ui.auth.pass_code.component.PassCodeComponent.Model
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject

public class PassComponentImpl(
    componentContext: ComponentContext,
    private val userHasPinCode: Boolean,
    private val onNavigateBack: () -> Unit
) : PassCodeComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val sharedPreferences by inject<SharedPreferencesSetting>()

    private val _model = MutableValue(
        initialValue = Model(
            pinCode = "",
            pinLength = 4,
            userHasPinCode = userHasPinCode
        )
    )

    override val model: Value<Model> = _model

    override fun fillPass(pinCode: String) {
        _model.update { it.copy(pinCode = pinCode) }
    }

    override fun updatePinCode(pinCode: String) {
        sharedPreferences.pincode = pinCode
        Log.d("behold123", sharedPreferences.pincode.toString())
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
        val maxLength = _model.value.pinLength

        if (currentPin.length < maxLength) {
            val newPin = currentPin + number
            _model.update { it.copy(pinCode = newPin) }

            if (newPin.length == maxLength) {
                saveOrVerifyPin(newPin)
            }
        }
    }

    override fun onDeleteClick() {
        val currentPin = _model.value.pinCode
        if (currentPin.isNotEmpty()) {
            val updatedPin = currentPin.dropLast(1)
            _model.update { it.copy(pinCode = updatedPin) }
        }
    }

    private fun saveOrVerifyPin(enteredPin: String) {
        if (userHasPinCode) {
            val storedPin = sharedPreferences.pincode
            if (enteredPin == storedPin) {
            } else {
                _model.update { it.copy(pinCode = "") }
            }
        } else {
            updatePinCode(enteredPin)
            _model.update { it.copy(pinCode = "") }
        }
    }
}