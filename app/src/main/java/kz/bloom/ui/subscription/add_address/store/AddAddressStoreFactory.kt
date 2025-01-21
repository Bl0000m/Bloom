package kz.bloom.ui.subscription.add_address.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.Intent
import kz.bloom.ui.subscription.add_address.store.AddAddressStore.State
import kotlin.coroutines.CoroutineContext


sealed interface Message: JvmSerializable {
    data object ErrorOccurred: Message
    data object AddressAdded: Message
}

sealed interface Action: JvmSerializable {

}

internal fun addAddressStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferencesSetting: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi
) : AddAddressStore =
    object : AddAddressStore, Store<Intent, State, Nothing>
            by storeFactory.create<Intent, Action, Message, State, Nothing>(
                name = "AddAddressStore",
                initialState = State(
                    isError = false,
                    addressCreated = false
                ),
                reducer = { message ->
                    when(message) {
                        is Message.ErrorOccurred -> { copy() }
                        is Message.AddressAdded -> { copy() }
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

}