package kz.bloom.ui.auth.country_chooser.component

import kotlinx.serialization.Serializable

@Serializable
public data class CountryModel(
    val code: String,
    val name: String,
    val dialCode: String,
    val flagEmoji: String,
    val phoneNumberLength: Int
)

public val CountryModel.isRussiaOrKazakhstan: Boolean get() =
    code == "KZ" || code == "RU"