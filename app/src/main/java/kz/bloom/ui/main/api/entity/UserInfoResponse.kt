package kz.bloom.ui.main.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val email: String
)

