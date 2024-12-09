package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionResponseBody(
    val id: Int,
    val name: String,
    val userName: String
)