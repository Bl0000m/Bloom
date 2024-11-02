package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class SignInTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshExpiresIn: Int,
    val refreshToken: String,
    val refreshTokenExpDate: String,
    val tokenType: String,
    val sessionState: String,
    val scope: String
)