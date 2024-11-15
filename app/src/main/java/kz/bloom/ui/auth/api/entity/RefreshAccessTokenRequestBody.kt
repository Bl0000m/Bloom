package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class RefreshAccessTokenRequestBody(
    val refreshToken: String
)