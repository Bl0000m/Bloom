package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)