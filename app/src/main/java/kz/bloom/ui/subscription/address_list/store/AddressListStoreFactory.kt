package kz.bloom.ui.subscription.address_list.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.address_list.store.AddressListStore.Intent
import kz.bloom.ui.subscription.address_list.store.AddressListStore.State
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext


sealed interface Message: JvmSerializable {
    data object ErrorOccurred: Message
    data object AddressesLoaded: Message
}

sealed interface Action : JvmSerializable {
    data object LoadAddresses: Action
}

internal fun addressListStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferencesSetting: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi
): AddressListStore =
    object : AddressListStore, Store<Intent, State, Nothing>
            by storeFactory.create<Intent, Action, Message, State, Nothing>(
                name = "AddressListStore",
                initialState = State(
                    isError = false
                ),
                reducer = { message ->
                    when(message) {
                        is Message.AddressesLoaded -> { copy() }
                        is Message.ErrorOccurred -> { copy() }
                    }
                },
                bootstrapper = SimpleBootstrapper(Action.LoadAddresses),
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

}