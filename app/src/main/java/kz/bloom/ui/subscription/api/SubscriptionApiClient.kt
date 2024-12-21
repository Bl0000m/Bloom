package kz.bloom.ui.subscription.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.order_list.store.Order

internal class SubscriptionApiClient(private val client: HttpClient) : SubscriptionApi {

    override suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String): CreateSubscriptionResponseBody {

        return client.post(SUBS_CREATE_SUBSCRIPTION) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
            setBody(requestBody)
        }.body<CreateSubscriptionResponseBody>()
    }

    override suspend fun loadOrderList(subscriptionId: Long, token: String): List<Order> {

        return client.get("$ORDER_LOAD/$subscriptionId") {
            headers.append("Authorization", "Bearer $token")
        }.body<List<Order>>()
    }

    override suspend fun loadBouquets(token: String) : List<BouquetDTO> {
        return client.get(BOUQUET) {
            headers.append("Authorization", "Bearer $token")
        }.body<List<BouquetDTO>>()
    }

    companion object {
        const val SUBS_CREATE_SUBSCRIPTION = "v1/client/subscription"
        const val ORDER_LOAD = "v1/client/order/subscription"
        const val BOUQUET = "v1/bouquet"
    }
}