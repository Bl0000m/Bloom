package kz.bloom.ui.subscription.address_list.component

import androidx.compose.runtime.MutableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.address_list.component.AddressListComponent.Model
import kz.bloom.ui.subscription.address_list.component.AddressListComponent.Address
import kz.bloom.ui.subscription.address_list.store.AddressListStore.State
import kz.bloom.ui.subscription.address_list.store.addressListStore
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.UserAddressDto
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

internal class AddressListComponentImpl(
    componentContext: ComponentContext,
    private val onAddAddress: () -> Unit,
    private val onNavigateBack: () -> Unit
) : KoinComponent, AddressListComponent, ComponentContext by componentContext {

    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = addressListStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferencesSetting = sharedPreferences,
        subscriptionApi = subscriptionApi
    )


    override val model: Value<Model> = store.states.toModels()

    private fun Value<State>.toModels(): Value<Model> {
        return map { state ->
            Model(
                addressList = state.userAddresses.toAddresses()
            )
        }
    }

    private fun List<UserAddressDto>.toAddresses(): List<Address> {
        return map { userAddressDto ->
            Address(
                street = userAddressDto.street,
                city = userAddressDto.city
            )
        }
    }

    override fun addAddress() {
        onAddAddress()
    }

    override fun navigateBack() {
        onNavigateBack()
    }
}