package kz.bloom.ui.subscription.address_list.component

import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface AddressListComponent {
    data class Model(
        val addressList: List<Address>
    )

    val model: Value<Model>

    data class Address(
        val street: String,
        val city: String
    )

    fun addAddress()

    fun navigateBack()
}