package kz.bloom.ui.subscription.add_address.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.State
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.Intent
import kz.bloom.ui.subscription.address_list.store.AddressListStore

interface AddAddressStore: Store<Intent, State, Nothing> {
    data class State(
        val isError: Boolean,
        val addressCreated: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class AddAddress(val addressDto: AddressDto) : Intent
    }

    @Serializable
    data class AddressDto(
        val street: String,
        val building: String,
        val apartment: String,
        val entrance: String,
        val intercom: String,
        val floor: Int,
        val city: String,
        val postalCode: String?,
        val latitude: String?,
        val longitude: String?,
        val orderId: Long,
        val recipientPhone: String,
        val comment: String
    )
}