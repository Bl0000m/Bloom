package kz.bloom.ui.auth.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendValidationCode(
    val email: String,
    val resetCode: String
)