package kz.bloom.ui.subscription.order_list.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.order_list.store.OrderListStore.Intent
import kz.bloom.ui.subscription.order_list.store.OrderListStore.State
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred: Message
    data class OrdersLoaded(val orders: List<Order>) : Message
}

private sealed interface Action : JvmSerializable {
    data class LoadOrderList(val subscriptionId: Long) : Action
}


internal fun orderListStoreFactory(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi,
    subscriptionId: Long
) : OrderListStore =
    object : OrderListStore, Store<Intent, State, Nothing>
by storeFactory.create<Intent, Action, Message, State,Nothing>(
    name = "OrderListStore",
    initialState = State(
        orderList = emptyList(),
        isLoading = false,
        isError = false
    ),
    reducer = { message ->
        when(message) {
            is Message.ErrorOccurred -> { copy(isError = true) }
            is Message.LoadingChanged -> { copy(isLoading = message.isLoading) }
            is Message.OrdersLoaded -> { copy(orderList = message.orders) }
        }
    },
    bootstrapper = SimpleBootstrapper(Action.LoadOrderList(subscriptionId = subscriptionId)),
    executorFactory = {
        ExecutorImpl(
            mainContext = mainContext,
            ioContext = ioContext,
            sharedPreferences = sharedPreferences,
            subscriptionApi = subscriptionApi
        )
    }
) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val subscriptionApi: SubscriptionApi,
    private val sharedPreferences: SharedPreferencesSetting
) : CoroutineExecutor<Intent, Action, State, Message, Nothing>(
    mainContext = mainContext
) {
    override fun executeAction(action: Action, getState: () -> State) {
        super.executeAction(action, getState)
        when(action) {
            is Action.LoadOrderList -> {
                scope.launch {
                    try {
                        val loadOrderList = withContext(context = ioContext) {
                            async { subscriptionApi.loadOrderList(subscriptionId = action.subscriptionId, token = sharedPreferences.accessToken!!) }
                        }
                        val orderListResponse = loadOrderList.await()
                        dispatch(message = Message.OrdersLoaded(orders = orderListResponse))
                    }
                    catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}