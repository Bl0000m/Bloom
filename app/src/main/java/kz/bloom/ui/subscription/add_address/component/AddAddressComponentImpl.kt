package kz.bloom.ui.subscription.add_address.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.combine
import kz.bloom.libraries.states
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent.Model
import kz.bloom.ui.subscription.add_address.store.AddAddressStore
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.AddressDto
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.Intent
import kz.bloom.ui.subscription.add_address.store.addAddressStore
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.State
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

internal class AddAddressComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack:() -> Unit,
    private val orderId: Long

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
            comment = ""
        )
    )

    init {
        store.states.subscribe { state ->
            if (state.city != "") {
                _model.update { it.copy(city = state.city) }
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
            floor = this.value.floor.toInt(),
            postalCode = null,
            latitude = null,
            longitude = null,
            orderId = orderId,
            recipientPhone = this.value.recipientPhoneNumber,
            comment = this.value.comment,
            city = this.value.city
        )
    }
}