package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserAddressDto(
    val id: Long,
    val street: String,
    val building: String,
    val apartment: String?,
    val entrance: String?,
    val intercom: String?,
    val floor: Int?,
    val city: String,
    val postalCode: String?,
    val latitude: Double?,
    val longitude: Double?
)
