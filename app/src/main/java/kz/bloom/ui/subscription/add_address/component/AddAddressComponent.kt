package kz.bloom.ui.subscription.add_address.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.country_chooser.component.CountryModel

interface AddAddressComponent {
    data class Model(
        val city: String,
        val street: String,
        val house: String,
        val apartment: String,
        val entry: String,
        val intercom: String,
        val floor: String,
        val recipientPhoneNumber: String,
        val comment: String,
        val selectedCountry: CountryModel,
        val isPrimaryButtonEnabled: Boolean
    )

    val model: Value<Model>

    fun onStreetFill(street: String)

    fun onHouseFill(house: String)

    fun onApartmentFill(apartment: String)

    fun onEntryFill(entry: String)

    fun onIntercomFill(intercom: String)

    fun onFloorFill(floor: String)

    fun onPhoneNumberFill(recipientPhoneNumber: String)

    fun onCommentFill(comment: String)

    fun createAddress()

    fun clearPhone()

    fun openCountryChooser()

    fun updateSelectedCountry(country: CountryModel)

    fun navigateBack()

}