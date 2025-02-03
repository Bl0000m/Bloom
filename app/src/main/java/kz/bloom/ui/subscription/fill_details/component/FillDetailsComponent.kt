package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.OrderDetails

interface FillDetailsComponent {
    data class Model(
        val orderDetails: OrderDetails,
        val bouquetDetailsLoaded: Boolean,
        val addressDetailsLoaded: Boolean,
        val deliveryDate: String
    )

    val model: Value<Model>

    fun chooseFlower()

    fun addressClicked()

    fun onClose()
}