package kz.bloom.ui.subscription.flower_details.store

import android.util.Log
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.flower_details.store.FlowerDetailsStore.State
import kz.bloom.ui.subscription.flower_details.store.FlowerDetailsStore.Intent
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data object ErrorOccurred : Message
    data class BouquetDetailsLoaded(val bouquetDetails: BouquetDetailsResponse) : Message
}

private sealed interface Action : JvmSerializable {
    data class LoadSpecificBouquet(val bouquetId: Long): Action
}
internal fun flowerDetailsStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi,
    bouquetDTO: BouquetDTO
) : FlowerDetailsStore =
    object : FlowerDetailsStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Action, Message, State, Nothing>(
        name = "FlowerDetailsStore",
        initialState = State(
            bouquetDetailsResponse = BouquetDetailsResponse(
                id = 0,
                name = "",
                author = "",
                bouquetPhotos = emptyList(),
                bouquetStyle = "",
                flowerVarietyInfo = emptyList(),
                additionalElements = emptyList(),
                branchBouquetInfo = emptyList()
            ),
            isError = false
        ),
        reducer = { message ->
            when(message) {
                is Message.BouquetDetailsLoaded -> copy(bouquetDetailsResponse = message.bouquetDetails)
                is Message.ErrorOccurred -> copy(isError = true)
            }
        },
        bootstrapper = SimpleBootstrapper(Action.LoadSpecificBouquet(bouquetId = bouquetDTO.id)),
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
        when(action) {
            is Action.LoadSpecificBouquet -> {
                scope.launch {
                    try {
                        val bouquetDetailsResponse = withContext(context = ioContext) {
                            subscriptionApi.loadBouquetDetails(
                                bouquetId = action.bouquetId,
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
