package kz.bloom.ui.subscription.add_address.component

import com.arkivanov.decompose.value.Value

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
        val comment: String
    )

    val model: Value<Model>

    fun onCityFill(city: String)

    fun onStreetFill(street: String)

    fun onHouseFill(house: String)

    fun onApartmentFill(apartment: String)

    fun onEntryFill(entry: String)

    fun onIntercomFill(intercom: String)

    fun onFloorFill(floor: String)

    fun onPhoneNumberFill(recipientPhoneNumber: String)

    fun onCommentFill(comment: String)

    fun createAddress()

}