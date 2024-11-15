package kz.bloom.ui.auth.country_chooser.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.country_chooser.Language
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.State
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.Intent
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.Label
import kz.bloom.ui.auth.country_chooser.store.entity.Country

internal interface CountriesStore : Store<Intent, State, Label> {
    data class State(
        val query: String,
        val userLanguage: Language,
        val countries: List<Country>,
        val filteredCountries: List<Country>,
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class SelectCountry(val countryName: String) : Intent
        data class FilterCountries(val query: String) : Intent
    }

    sealed interface Label : JvmSerializable {
        data class CountrySelected(val country: Country) : Label
    }
}