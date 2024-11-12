package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetValidationCode(
    val email: String
)