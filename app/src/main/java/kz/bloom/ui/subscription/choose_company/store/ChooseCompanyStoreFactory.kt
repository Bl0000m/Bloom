package kz.bloom.ui.subscription.choose_company.store

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
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.State
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.Intent
import kotlin.coroutines.CoroutineContext

private sealed interface Message : JvmSerializable {
    data object ErrorOccurred : Message
    data object OrderFilled : Message
    data class CompaniesRawLoaded(val companiesRaw: BouquetDetailsResponse) : Message
}

private sealed interface Action : JvmSerializable {
    data class LoadCompanies(val bouquetId: Long) : Action
}

internal fun chooseCompanyStore(
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory,
    sharedPreferences: SharedPreferencesSetting,
    subscriptionApi: SubscriptionApi,
    bouquetId: Long
): ChooseCompanyStore =
    object : ChooseCompanyStore, Store<Intent, State, Nothing>
    by storeFactory.create<Intent, Action, Message, State, Nothing>(
        name = "ChooseCompanyStore",
        initialState = State(
            companiesRaw = BouquetDetailsResponse(
                id = 0,
                name = "",
                author = "",
                bouquetPhotos = emptyList(),
                bouquetStyle = "",
                flowerVarietyInfo = emptyList(),
                additionalElements = emptyList(),
                branchBouquetInfo = emptyList()
            ),
            isError = false,
            orderFilled = false
        ),
        reducer = { message ->
            when (message) {
                is Message.ErrorOccurred -> copy(isError = true)
                is Message.CompaniesRawLoaded -> copy(companiesRaw = message.companiesRaw)
                is Message.OrderFilled -> copy(orderFilled = true)
            }
        },
        bootstrapper = SimpleBootstrapper(Action.LoadCompanies(bouquetId = bouquetId)),
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
            is Action.LoadCompanies -> {
                scope.launch {
                    try {
                        val companyDetailsResponse = withContext(context = ioContext) {
                            subscriptionApi.loadBouquetDetails(
                                bouquetId = action.bouquetId,
                                token = sharedPreferences.accessToken!!
                            )
                        }
                        dispatch(message = Message.CompaniesRawLoaded(companyDetailsResponse))
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when(intent) {
            is Intent.FillOrder -> {
                scope.launch {
                    try {
                        val fillOrderResponse = withContext(context = ioContext) {
                            subscriptionApi.fillOrder(
                                orderRequestBody = intent.orderRequestBody,
                                token = sharedPreferences.accessToken!!
                            )
                        }
                        dispatch(message = Message.OrderFilled)
                    } catch (e: Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}