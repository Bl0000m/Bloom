package kz.bloom.ui.auth.pass_code.component

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
    private val onNavigateBack:() -> Unit
) : PassCodeComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val sharedPreferences by inject<SharedPreferencesSetting>()

    private val _model = MutableValue(
        initialValue = Model(
            pinCode = ""
        )
    )

    override val model: Value<Model> = _model

    override fun fillPass(pinCode: String) {
        _model.update { it.copy(pinCode = pinCode) }
    }

    override fun updatePinCode(pinCode: String) {
        sharedPreferences.pincode = pinCode
    }
}