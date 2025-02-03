package kz.bloom.ui.subscription.fill_details.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.State
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.Intent
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.OrderDetails
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.BouquetInfo1
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.BranchDivisionInfoDto
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data object ErrorOccurred : Message
    data object AddressBeenFilled : Message
    data class BouquetDetailsLoaded(val orderDetails: OrderDetails) : Message
    data class AddressDetailsLoaded(val orderDetails: OrderDetails) : Message
}

private sealed interface Action : JvmSerializable {

}

internal fun fillDetailsStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferencesSetting: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi
): FillDetailsStore =
    object : FillDetailsStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Action, Message, State, Nothing>(
        name = "FillDetailsStore",
        initialState = State(
            isError = false,
            addressBeenFilled = false,
            orderDetails = OrderDetails(
                id = 0,
                orderCode = 0,
                address = FillDetailsStore.DetailAddressDto(
                    id = 0,
                    city = "",
                    street = "",
                    building = "",
                    apartment = "",
                    entrance = "",
                    intercom = "",
                    floor = null,
                    postalCode = null,
                    latitude = null,
                    longitude = null,
                    recipientPhone = "",
                    comment = ""
                ),
                bouquetInfo = BouquetInfo1(
                    id = 0,
                    name = "",
                    bouquetPhotos = emptyList()
                ),
                deliveryDate = "",
                deliveryStartTime = "",
                deliveryEndTime = "",
                orderStatus = "",
                assemblyCost = 0.0,
                branchDivisionInfoDto = BranchDivisionInfoDto(
                    id = 0,
                    address = "",
                    divisionType = "",
                    phoneNumber = "",
                    email = ""
                )
            ),
            addressDetailsLoaded = false,
            bouquetDetailsLoaded = false
        ),
        reducer = { message ->
            when (message) {
                is Message.ErrorOccurred -> copy(isError = true)
                is Message.AddressBeenFilled -> copy(addressBeenFilled = true)
                is Message.AddressDetailsLoaded -> copy(orderDetails = message.orderDetails, addressDetailsLoaded = true)
                is Message.BouquetDetailsLoaded -> copy(orderDetails = message.orderDetails, bouquetDetailsLoaded = true)
            }
        },
        bootstrapper = SimpleBootstrapper(),
        executorFactory = {
            ExecutorImpl(
                mainContext = mainContext,
                ioContext = ioContext,
                sharedPreferencesSetting = sharedPreferencesSetting,
                subscriptionApi = subscriptionApi
            )
        }
    ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val sharedPreferencesSetting: SharedPreferencesSetting,
    private val subscriptionApi: SubscriptionApi
) : CoroutineExecutor<Intent, Action, State, Message, Nothing>(
    mainContext = mainContext
) {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when (intent) {
            is Intent.LoadOrderDetails -> {
                scope.launch {
                    try {
                        val orderDetails = withContext(context = ioContext) {
                            subscriptionApi.getOrderDetails(
                                orderId = intent.orderId,
                                token = sharedPreferencesSetting.accessToken!!
                            )
                        }
                        if (orderDetails.address != null) {
                            dispatch(message = Message.AddressDetailsLoaded(orderDetails = orderDetails))
                        } else if (orderDetails.bouquetInfo!= null) {
                            dispatch(message = Message.BouquetDetailsLoaded(orderDetails = orderDetails))
                        }
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
            is Intent.FillAddress -> {

            }
        }
    }
}