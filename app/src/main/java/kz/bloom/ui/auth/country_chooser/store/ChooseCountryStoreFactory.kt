package kz.bloom.ui.auth.country_chooser.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kz.bloom.ui.auth.country_chooser.AssetsProvider
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.Intent
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.Label
import kz.bloom.ui.auth.country_chooser.store.CountriesStore.State
import kz.bloom.ui.auth.country_chooser.Language
import kz.bloom.ui.auth.country_chooser.store.entity.Country
import kotlin.coroutines.CoroutineContext

private const val COUNTRIES_LIST_FILE_NAME = "PhoneExtensions.json"

private sealed interface CountriesAction : JvmSerializable {
    data object LoadCountries : CountriesAction
}

private sealed interface CountriesMessage : JvmSerializable {
    data class CountriesLoaded(val countries: List<Country>) : CountriesMessage
    data class CountriesFiltering(val query: String) : CountriesMessage
    data class UserLanguageChanged(val language: Language) : CountriesMessage
}

@OptIn(ExperimentalMviKotlinApi::class)
internal fun CountriesStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    assetsProvider: AssetsProvider,
    //sharedPreferences: SharedPreferencesSetting,
) : CountriesStore =
    object : CountriesStore, Store<Intent, State, Label>
    by storeFactory.create<Intent, CountriesAction, CountriesMessage, State, Label>(
        name = "CountriesStore",
        reducer = { message ->
            when (message) {
                is CountriesMessage.CountriesLoaded -> copy(
                    countries = message.countries,
                    filteredCountries = message.countries
                )
                is CountriesMessage.CountriesFiltering -> copy(
                    query = message.query,
                    filteredCountries = countries.filter { country ->
                        country.localizedName
                            .lowercase(locale = Locale.getDefault())
                            .startsWith(
                                prefix = message.query.lowercase(
                                    locale = Locale.getDefault()
                                )
                            )
                    }
                )
                is CountriesMessage.UserLanguageChanged -> {
                    val language = message.language
                    val translatedCountries = countries

                    for (country in translatedCountries) {
                        country.setLocalizedName(
                            userPreferredLanguage = language
                        )
                    }

                    copy(
                        userLanguage = language,
                        countries = translatedCountries
                    )
                }
            }
        },
        bootstrapper = SimpleBootstrapper(
            CountriesAction.LoadCountries
        ),
        initialState = State(
            query = "",
            countries = emptyList(),
            filteredCountries = emptyList(),
            userLanguage = Language.fromLanguageCode(
                "ru"
                //code = sharedPreferences.getLanguage()
            ),
        ),
        executorFactory = coroutineExecutorFactory(mainContext = mainContext) {
            onAction<CountriesAction.LoadCountries> {
                launch {
                    val countries = withContext(context = ioContext) {
                        Json.decodeFromString<List<Country>>(
                            string = assetsProvider.parseAssetsFileContents(
                                fileName = COUNTRIES_LIST_FILE_NAME
                            )
                        )
                    }.also { countries ->
                        for (country in countries) {
                            country.setLocalizedName(
                                userPreferredLanguage = state.userLanguage
                            )
                        }
                    }

                    dispatch(
                        message = CountriesMessage.CountriesLoaded(
                            countries = countries
                        )
                    )
                }
            }

            onIntent<Intent.SelectCountry> { intent ->
                val selectedCountry = state.filteredCountries
                    .first { country -> country.localizedName == intent.countryName }

                publish(
                    label = Label.CountrySelected(
                        country = selectedCountry
                    )
                )
            }

            onIntent<Intent.FilterCountries> { intent ->
                dispatch(
                    message = CountriesMessage.CountriesFiltering(
                        query = intent.query
                    )
                )
            }
        }
    ) { }