package kz.bloom.ui.subscription.choose_flower.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.State
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.Intent
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred : Message
    data class BouquetsLoaded(val bouquets: List<BouquetDTO>) : Message
    data class BouquetDetailsLoaded(val bouquetDetails: BouquetDetailsResponse) : Message
}

private sealed interface Action : JvmSerializable {
    data object LoadBouquets : Action
}

internal fun chooseFlowerStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi
): ChooseFlowerStore =
    object : ChooseFlowerStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Action, Message, State, Nothing>(
        name = "ChooseFlowerStore",
        initialState = State(
            bouquets = emptyList(),
            bouquetDetails = null,
            isLoading = false,
            isError = false
        ),
        reducer = { message ->
            when (message) {
                is Message.ErrorOccurred -> {
                    copy(isError = true)
                }

                is Message.LoadingChanged -> {
                    copy(isLoading = message.isLoading)
                }

                is Message.BouquetsLoaded -> {
                    copy(bouquets = message.bouquets)
                }

                is Message.BouquetDetailsLoaded -> {
                    copy(bouquetDetails = message.bouquetDetails)
                }
            }
        },
        bootstrapper = SimpleBootstrapper(Action.LoadBouquets),
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
    private val sharedPreferences: SharedPreferencesSetting,
    private val subscriptionApi: SubscriptionApi
) : CoroutineExecutor<Intent, Action, State, Message, Nothing>(
    mainContext = mainContext
) {
    override fun executeAction(action: Action, getState: () -> State) {
        super.executeAction(action, getState)
        when (action) {
            is Action.LoadBouquets -> {
                scope.launch {
                    try {
                        val bouquetResponse = withContext(context = ioContext) {
                            subscriptionApi.loadBouquets(token = sharedPreferences.accessToken!!)
                        }
                        dispatch(message = Message.BouquetsLoaded(bouquetResponse))
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when (intent) {
            is Intent.LoadSpecificBouquet -> {
                scope.launch {
                    try {
                        val bouquetDetailsResponse = withContext(context = ioContext) {
                            subscriptionApi.loadBouquetDetails(
                                bouquetId = intent.id,
                                token = sharedPreferences.accessToken!!
                            )
                        }
                        dispatch(message = Message.BouquetDetailsLoaded(bouquetDetailsResponse))
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}