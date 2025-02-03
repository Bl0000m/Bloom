package kz.bloom.ui.subscription.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType
import kz.bloom.ui.subscription.add_address.store.AddAddressStore
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.CreateOrderRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody
import kz.bloom.ui.subscription.api.entity.UserAddressDto
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.OrderDetails
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

    override suspend fun loadBouquetDetails(bouquetId: Long, token: String): BouquetDetailsResponse {
        return client.get("$BOUQUET_DETAILS/$bouquetId") {
            headers.append("Authorization", "Bearer $token")
        }.body<BouquetDetailsResponse>()
    }

    override suspend fun fillOrder(
        orderRequestBody: CreateOrderRequestBody,
        token: String
    ): HttpResponse {
        return client.post(FILL_ORDER) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
            setBody(orderRequestBody)
        }
    }

    override suspend fun getOrderDetails(orderId: Long, token: String): OrderDetails {
        return client.get("$GET_ORDER_DETAILS/$orderId") {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
        }.body<OrderDetails>()
    }

    override suspend fun createOrderAddress(
        addressRequestBody: AddAddressStore.AddressDto,
        token: String
    ): HttpResponse {
        return client.post(CREATE_ORDER_ADDRESS) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
            setBody(addressRequestBody)
        }
    }

    override suspend fun loadUserAddresses(token: String): List<UserAddressDto> {
        return client.get(GET_USER_ADDRESSES) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
        }.body<List<UserAddressDto>>()
    }

    companion object {
        const val SUBS_CREATE_SUBSCRIPTION = "v1/client/subscription"
        const val ORDER_LOAD = "v1/client/order/subscription"
        const val BOUQUET = "v1/bouquet"
        const val BOUQUET_DETAILS = "v1/bouquet"
        const val FILL_ORDER = "v1/client/order"
        const val GET_ORDER_DETAILS = "v1/client/order"
        const val CREATE_ORDER_ADDRESS = "v1/address/order"
        const val GET_USER_ADDRESSES = "v1/users/my-address"
    }
}