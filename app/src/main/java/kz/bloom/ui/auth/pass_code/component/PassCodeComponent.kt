package kz.bloom.ui.auth.pass_code.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

public interface PassCodeComponent {
    public val model: Value<Model>

    public data class Model(
        val pinCode: String,
        val confirmPinCode: String,
        val pinLength: Int,
        val pinCodeMissMatch: Boolean
    )

    public sealed interface Event {
        public data class DisplaySnackBar(
            public val errorMessage: String
        ) : Event
    }

    public val events: Flow<Event>

    public fun fillPass(pinCode: String)

    public fun updatePinCode(pinCode: String)

    public fun onBackClick()

    public fun onCloseClick()

    public fun onNumberClick(number: Int)

    public fun onDeleteClick()
}