package kz.bloom.ui.subscription.fill_details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.AddressDto
import kz.bloom.ui.subscription.api.entity.CreateOrderRequestBody
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.State
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.Intent

interface FillDetailsStore : Store<Intent, State, Nothing> {
    data class State(
        val isError: Boolean,
        val orderDetails: OrderDetails,
        val addressBeenFilled: Boolean,
        val detailsLoaded: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class FillAddress(val orderRequestBody: CreateOrderRequestBody) : Intent
        data class LoadOrderDetails(val orderId: Long) : Intent
    }

    @Serializable
    data class OrderDetails(
        val id: Long,
        val orderCode: Long? = null,
        val address: AddressDto?,
        val bouquetInfo: BouquetInfo1? = null,
        val branchDivisionInfoDto: BranchDivisionInfoDto,
        val assemblyCost: Double,
        val deliveryDate: String,
        val deliveryStartTime: String,
        val deliveryEndTime: String,
        val orderStatus: String
    )

    @Serializable
    data class BranchDivisionInfoDto(
        val id: Long,
        val address: String,
        val divisionType: String,
        val phoneNumber: String,
        val email: String
    )

    @Serializable
    data class BouquetInfo1(
        val id: Long,
        val name: String,
        val bouquetPhotos: List<BouquetPhoto>
    )

    @Serializable
    data class BouquetPhoto(
        val id: Long,
        val url: String
    )
}