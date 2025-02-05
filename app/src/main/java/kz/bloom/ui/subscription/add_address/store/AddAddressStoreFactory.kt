package kz.bloom.ui.subscription.add_address.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.Intent
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.State
import kotlin.coroutines.CoroutineContext


sealed interface Message: JvmSerializable {
    data object ErrorOccurred: Message
    data object AddressCreated: Message
    data class CityNameLoaded(val cityName: String) : Message
    data object AddressCreateStateRefreshed: Message
}

sealed interface Action: JvmSerializable {
    data class LoadUserCityName(val orderId: Long) : Action
}

internal fun addAddressStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferencesSetting: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi,
    orderId: Long
) : AddAddressStore =
    object : AddAddressStore, Store<Intent, State, Nothing>
            by storeFactory.create<Intent, Action, Message, State, Nothing>(
                name = "AddAddressStore",
                initialState = State(
                    isError = false,
                    addressCreated = false,
                    city = ""
                ),
                reducer = { message ->
                    when(message) {
                        is Message.ErrorOccurred -> { copy(isError = true) }
                        is Message.AddressCreated -> { copy(addressCreated = true) }
                        is Message.CityNameLoaded -> { copy(city = message.cityName) }
                        is Message.AddressCreateStateRefreshed -> {
                            Log.d("behold1", "refreshed")
                            copy(addressCreated = false)
                        }
                    }
                },
                bootstrapper = SimpleBootstrapper(Action.LoadUserCityName(orderId)),
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
        when(intent) {
            is Intent.AddAddress -> {
                scope.launch {
                    try {
                        val addAddressResponse = withContext(context = ioContext) {
                            subscriptionApi.createOrderAddress(
                                addressRequestBody = intent.addressDto,
                                token = sharedPreferencesSetting.accessToken!!)
                        }
                        if (addAddressResponse.status.value == 200) {
                            dispatch(message = Message.AddressCreated)
                        }
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
            is Intent.RefreshAddressCreatedState -> {
                scope.launch {
                    dispatch(message = Message.AddressCreateStateRefreshed)
                }
            }
        }
    }

    override fun executeAction(action: Action, getState: () -> State) {
        super.executeAction(action, getState)
        when(action) {
            is Action.LoadUserCityName -> {
                scope.launch {
                    try {
                        val getBranchCityString = withContext(ioContext) {
                            subscriptionApi.getOrderDetails(
                                orderId = 721,
                                //action.orderId,
                                token = sharedPreferencesSetting.accessToken!!
                            )
                        }
                        dispatch(message = Message.CityNameLoaded(getBranchCityString.branchDivisionInfoDto.address))
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}