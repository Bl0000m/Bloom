package kz.bloom.ui.subscription.order_list.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import kz.bloom.ui.subscription.api.SubscriptionApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.order_list.component.OrderListComponent.Model
import kz.bloom.ui.subscription.order_list.store.orderListStoreFactory
import kz.bloom.ui.ui_components.coroutineScope
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class OrderListComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateRightBack:() -> Unit,
    private val onFillOrder: () -> Unit,
    subscriptionId: Long
) : OrderListComponent, KoinComponent, ComponentContext by componentContext
{
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = orderListStoreFactory(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferences = sharedPreferences,
        subscriptionApi = subscriptionApi,
        subscriptionId = subscriptionId
    )
    private val scope = coroutineScope()

    private val _model = MutableValue(
        initialValue = Model(
            orders = emptyList(),
        )
    )

    init {
        scope.launch {
            store.states.collect { stateOrder ->
                _model.update { it.copy(orders = stateOrder.orderList) }
            }
        }
    }

    override val model: Value<Model> = _model

    override fun onNavigateToRightBack() {
        onNavigateRightBack()
    }

    override fun openIndividualOrder() {
        onFillOrder()
    }
}