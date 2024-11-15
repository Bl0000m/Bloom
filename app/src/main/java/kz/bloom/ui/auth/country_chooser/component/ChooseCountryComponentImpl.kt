package kz.bloom.ui.auth.country_chooser.component

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.auth.country_chooser.AssetsProvider
import kz.bloom.ui.auth.country_chooser.component.ChooseCountryComponent.Model
import kz.bloom.ui.auth.country_chooser.store.CountriesStore
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.Intent
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.State
import kz.bloom.ui.auth.country_chooser.store.entity.Country
import kz.bloom.ui.ui_components.parseAssetsFileContents
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

public class ChooseCountryComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    private val onCountrySelected:(CountryModel) -> Unit,
    private val onNavigateBack:() -> Unit
) : ChooseCountryComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val storeFactory by inject<StoreFactory>()
    //private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))

    private val assetsProvider = AssetsProvider { fileName ->
        context.parseAssetsFileContents(
            fileName = fileName
        )
    }

    private val store = instanceKeeper.getStore {
        CountriesStore(
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            assetsProvider = assetsProvider,
        )
    }

    override val model: Value<Model> = store.states.toModel()

    override fun clearQuery() {
        store.accept(
            intent = Intent.FilterCountries(
                query = ""
            )
        )
    }

    override fun selectCountry(country: CountryModel) {
        onCountrySelected(country)
        store.accept(
            intent = Intent.SelectCountry(
                countryName = country.name
            )
        )
    }

    override fun filterCountries(query: String) {
        store.accept(
            intent = Intent.FilterCountries(
                query = query
            )
        )
    }

    override fun navigateBack() {
        onNavigateBack()
    }
}

public fun Country.toCountryModel(): CountryModel =
    CountryModel(
        code = code,
        name = localizedName,
        dialCode = dialCode,
        flagEmoji = flagUnicode,
        phoneNumberLength = phoneNumberLength
    )

public fun CountryModel.countryModelToCountry(nameInRussian: String, nameInEnglish: String): Country =
    Country(
        code = code,
        dialCode = dialCode,
        flagUnicode = flagEmoji,
        nameInRussian = nameInRussian,
        nameInEnglish = nameInEnglish,
        phoneNumberLength = phoneNumberLength,
    )

private fun Value<State>.toModel(): Value<Model> = map { state ->
    Model(
        query = state.query,
        countries = state.filteredCountries.toCountryModels()
    )
}

private fun List<Country>.toCountryModels(): List<CountryModel> {
    val countryModels = mutableListOf<CountryModel>()

    for ((index, country) in this.withIndex()) {
        countryModels += CountryModel(
            code = country.code,
            name = country.localizedName,
            dialCode = country.dialCode,
            flagEmoji = country.flagUnicode,
            phoneNumberLength = country.phoneNumberLength
        )
    }

    return countryModels
}