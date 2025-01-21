package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequestBody(
    val id: Long,
    val bouquetId: Long,
    val branchDivisionId: Long,
    val assemblyCost: Double,
    val address: String
)
