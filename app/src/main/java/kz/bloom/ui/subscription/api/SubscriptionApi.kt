package kz.bloom.ui.subscription.api

import io.ktor.client.statement.HttpResponse
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionResponseBody

interface SubscriptionApi {
    suspend fun createSubscription(requestBody: CreateSubscriptionRequestBody, token: String) : CreateSubscriptionResponseBody
}