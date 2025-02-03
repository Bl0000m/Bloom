package kz.bloom.ui.subscription.add_address.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.auth.country_chooser.component.CountryModel
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent.Model
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.AddressDto
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.Intent
import kz.bloom.ui.subscription.add_address.store.addAddressStore
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

internal class AddAddressComponentImpl(
    componentContext: ComponentContext,
    selectedCountry: CountryModel? = null,
    private val onNavigateBack:() -> Unit,
    private val orderId: Long,
    private val onOpenCountryChooser: () -> Unit,
    private val onNavigateToOrderDetails: () -> Unit
) : AddAddressComponent, KoinComponent, ComponentContext by componentContext {

    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = addAddressStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferencesSetting = sharedPreferences,
        subscriptionApi = subscriptionApi,
        orderId = orderId
    )

    private val startingCountryModel = CountryModel(
        code = "KZ",
        name = "Казахстан",
        dialCode = "+7",
        flagEmoji = "\uD83C\uDDF0\uD83C\uDDFF",
        phoneNumberLength = 10
    )

    private val _model = MutableValue(
        initialValue = Model(
            city = "",
            street = "",
            house = "",
            apartment = "",
            entry = "",
            intercom = "",
            floor = "",
            recipientPhoneNumber = "",
            comment = "",
            selectedCountry = selectedCountry ?: startingCountryModel,
            isPrimaryButtonEnabled = false
        )
    )

    val scope = coroutineScope()
    init {
        store.states.subscribe { state ->
            if (state.city != "") {
                _model.update { it.copy(city = state.city) }
            }
            if (state.addressCreated) {
                scope.launch {
                    delay(timeMillis = 300L)
                    onNavigateToOrderDetails()
                }
            }
        }
        _model.subscribe { model ->
            if (model.street.isNotEmpty()
                && model.house.isNotEmpty()
                && model.recipientPhoneNumber.isNotEmpty()
                && !model.isPrimaryButtonEnabled
                ) {
                _model.update { it.copy(isPrimaryButtonEnabled = true) }
            }
        }
    }

    override val model: Value<Model> = _model

    override fun onStreetFill(street: String) {
        _model.update { it.copy(street = street) }
    }

    override fun onHouseFill(house: String) {
        _model.update { it.copy(house = house) }
    }

    override fun onApartmentFill(apartment: String) {
        _model.update { it.copy(apartment = apartment) }
    }

    override fun onEntryFill(entry: String) {
        _model.update { it.copy(entry = entry) }
    }

    override fun onIntercomFill(intercom: String) {
        _model.update { it.copy(intercom = intercom) }
    }

    override fun onFloorFill(floor: String) {
        _model.update { it.copy(floor = floor) }
    }

    override fun onPhoneNumberFill(recipientPhoneNumber: String) {
        _model.update { it.copy(recipientPhoneNumber = recipientPhoneNumber) }
    }

    override fun onCommentFill(comment: String) {
        _model.update { it.copy(comment = comment) }
    }

    override fun clearPhone() {
        _model.update { it.copy(recipientPhoneNumber = "") }
    }

    override fun openCountryChooser() {
        onOpenCountryChooser()
    }

    override fun updateSelectedCountry(country: CountryModel) {
        _model.update { it.copy(selectedCountry = country) }
    }

    override fun createAddress() {
        store.accept(
            intent = Intent.AddAddress(
                _model.toAddressDto(orderId = orderId)
            )
        )
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    private fun MutableValue<Model>.toAddressDto(orderId: Long) : AddressDto {
        return AddressDto(
            street = this.value.street,
            building = this.value.house,
            apartment = this.value.apartment,
            entrance = this.value.entry,
            intercom = this.value.intercom,
            floor = null,
            postalCode = null,
            latitude = null,
            longitude = null,
            orderId = orderId,
            recipientPhone = this.value.selectedCountry.dialCode + this.value.recipientPhoneNumber,
            comment = this.value.comment,
            city = this.value.city
        )
    }
}