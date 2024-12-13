package kz.bloom.ui.subscription.api

import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody
import kz.bloom.ui.subscription.order_list.store.Order

interface SubscriptionApi {
    suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String) : CreateSubscriptionResponseBody
    suspend fun loadOrderList(subscriptionId: Long, token: String) : List<Order>
}