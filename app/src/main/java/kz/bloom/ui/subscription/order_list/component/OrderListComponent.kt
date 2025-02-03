package kz.bloom.ui.subscription.order_list.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.order_list.store.Order

interface OrderListComponent {
    data class Model(
        val orders: List<Order>
    )

    val model: Value<Model>

    fun onNavigateToRightBack()

    fun openIndividualOrder(orderId: Long, deliveryDate: String)
}