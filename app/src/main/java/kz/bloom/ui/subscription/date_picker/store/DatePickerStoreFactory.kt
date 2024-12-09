package kz.bloom.ui.subscription.date_picker.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.CreateSubscriptionRequestBody
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.Intent
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.State
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data class LoadingChanged(val isLoading: Boolean) : Message
    data object ErrorOccurred: Message
}

internal fun DatePickerStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi
) : DatePickerStore =
    object : DatePickerStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Nothing, Message, State, Nothing>(
        name = "DatePickerStore",
        initialState = State(
            isError = false,
            isLoading = false,
            subscriptionCreated = false,
        ),
        reducer = { message ->
            when(message) {
                is Message.ErrorOccurred -> { copy(isError = true) }
                is Message.LoadingChanged -> { copy(isLoading = message.isLoading) }
            }
        },
        bootstrapper = SimpleBootstrapper(),
        executorFactory = {
            ExecutorImpl(
                mainContext = mainContext,
                ioContext = ioContext,
                sharedPreferences = sharedPreferences,
                subsApi = subscriptionApi
            )
        }
    ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val subsApi: SubscriptionApi,
    private val sharedPreferences: SharedPreferencesSetting
) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing>(
    mainContext = mainContext
) {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when(intent) {
            is Intent.CreateSubscription -> {
                scope.launch {
                    try {
                        val createSubscription = withContext(context = ioContext) {
                            subsApi.createSubscription(
                                requestBody = CreateSubscriptionRequestBody(
                                    userId = intent.userId.toInt(),
                                    name = intent.subscriptionName,
                                    subscriptionTypeId = intent.subscriptionTypeId.toInt(),
                                    orderDates = intent.dates
                                ),
                                token = sharedPreferences.accessToken!!
                            )
                        }
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}

