package kz.bloom.ui.auth.pass_code.component

import com.arkivanov.decompose.value.Value

public interface PassCodeComponent {
    public val model: Value<Model>

    public data class Model(
        val pinCode: String,
        val pinLength: Int,
        val userHasPinCode: Boolean
    )

    public fun fillPass(pinCode: String)

    public fun updatePinCode(pinCode: String)

    public fun onBackClick()

    public fun onCloseClick()

    public fun onNumberClick(number: Int)

    public fun onDeleteClick()
}