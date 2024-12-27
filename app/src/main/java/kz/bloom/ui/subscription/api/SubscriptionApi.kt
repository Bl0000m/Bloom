package kz.bloom.ui.subscription.api

import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.order_list.store.Order

interface SubscriptionApi {
    suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String) : CreateSubscriptionResponseBody
    suspend fun loadOrderList(subscriptionId: Long, token: String) : List<Order>
    suspend fun loadBouquets(token: String) : List<BouquetDTO>
    suspend fun loadBouquetDetails(bouquetId: Long, token: String) : BouquetDetailsResponse
}