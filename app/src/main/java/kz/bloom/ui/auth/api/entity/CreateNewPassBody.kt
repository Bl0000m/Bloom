package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CreateNewPassBody(
    val email: String,
    val newPassword: String,
    val confirmNewPassword: String
)