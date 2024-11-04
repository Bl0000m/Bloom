package kz.bloom.ui.country_chooser.component

import com.arkivanov.decompose.value.Value

public interface ChooseCountryComponent {
    public data class Model(
        public val query: String,
        public val countries: List<CountryModel>
    )

    public val model: Value<Model>

    public fun clearQuery()

    public fun selectCountry(country: CountryModel)

    public fun filterCountries(query: String)

    public fun navigateBack()
}