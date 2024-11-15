package kz.bloom.ui.auth.country_chooser

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

public enum class Language(public val code: String) : JvmSerializable {
    Unknown(code = ""),

    Kazakh(code = "kk"),
    Russian(code = "ru"),
    English(code = "en");

    public companion object {
        public fun fromLanguageCode(code: String): Language =
            when (code) {
                "kk" -> Kazakh
                "ru" -> Russian
                "en" -> English
                else -> English
            }
    }
}