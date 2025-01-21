package kz.bloom.ui.subscription.api

import io.ktor.client.statement.HttpResponse
import kz.bloom.ui.subscription.add_address.store.AddAddressStore
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.CreateOrderRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.OrderDetails
import kz.bloom.ui.subscription.order_list.store.Order

interface SubscriptionApi {
    suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String) : CreateSubscriptionResponseBody
    suspend fun loadOrderList(subscriptionId: Long, token: String) : List<Order>
    suspend fun loadBouquets(token: String) : List<BouquetDTO>
    suspend fun loadBouquetDetails(bouquetId: Long, token: String) : BouquetDetailsResponse
    suspend fun fillOrder(orderRequestBody: CreateOrderRequestBody, token: String) : HttpResponse
    suspend fun getOrderDetails(orderId: Long, token: String) : OrderDetails
    suspend fun createOrderAddress(addressRequestBody: AddAddressStore.AddressDto, token: String) : HttpResponse
}