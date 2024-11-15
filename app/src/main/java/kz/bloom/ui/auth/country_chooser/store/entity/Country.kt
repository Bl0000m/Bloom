package kz.bloom.ui.auth.country_chooser.store.entity

import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Transient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import kz.bloom.ui.auth.country_chooser.Language

@Serializable
public data class Country(
    @SerialName("code")
    public val code: String,

    @SerialName("dial_code")
    public val dialCode: String,

    @SerialName("flag")
    public val flagUnicode: String,

    @SerialName("size")
    public val phoneNumberLength: Int,

    @SerialName("name_en")
    public val nameInEnglish: String,

    @SerialName("name_ru")
    public val nameInRussian: String,
) : JvmSerializable {
    @Transient
    public lateinit var localizedName: String
        private set

    public fun setLocalizedName(userPreferredLanguage: Language) {
        localizedName = when (userPreferredLanguage) {
            Language.Kazakh, Language.Russian -> nameInRussian
            Language.English -> nameInEnglish
            else -> nameInEnglish
        }
    }
}