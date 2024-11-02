package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String
)