package kz.bloom.ui.subscription.order_list.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.order_list.store.OrderListStore.State
import kz.bloom.ui.subscription.order_list.store.OrderListStore.Intent

interface OrderListStore : Store<Intent, State, Nothing> {
    data class State(
        val orderList: List<Order>,
        val isLoading: Boolean,
        val isError: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {

    }
}

@Serializable
data class Order(
    val id: Long,
    val orderCode: Long? = null,
    val address: String? = null,
    val bouquetInfo: BouquetInfo? = null,
    val deliveryDate: String,
    val deliveryStartTime: String,
    val deliveryEndTime: String,
    val orderStatus: String? = null
)

@Serializable
data class BouquetInfo(
    val id: Long,
    val name: String,
    val description: String,
    val companyName: String,
    val bouquetPhotos: List<BouquetPhoto>,
    val price: Double,
    val addition: String
)

@Serializable
data class BouquetPhoto(
    val id: Long,
    val url: String
)