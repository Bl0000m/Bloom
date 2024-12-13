package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionResponseBody(
    val id: Long,
    val name: String,
    val userName: String
)