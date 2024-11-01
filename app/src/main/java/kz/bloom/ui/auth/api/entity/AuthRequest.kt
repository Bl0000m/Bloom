package kz.bloom.ui.auth.api.entity

data class AuthRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String
)