package kz.bloom.ui.subscription.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody

internal class SubscriptionApiClient(private val client: HttpClient) : SubscriptionApi {

    override suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String): CreateSubscriptionResponseBody {

        return client.post(SUBS_CREATE_SUBSCRIPTION) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $token")
            setBody(requestBody)
        }.body<CreateSubscriptionResponseBody>()
    }

    companion object {
        const val SUBS_CREATE_SUBSCRIPTION = "v1/client/subscription"
    }
}