package kz.bloom.ui.subscription.fill_details.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.subscription.api.SubscriptionApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent.Model
import kz.bloom.ui.subscription.fill_details.store.fillDetailsStore
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.State
import kz.bloom.ui.subscription.fill_details.store.FillDetailsStore.Intent
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class FillDetailsComponentImpl(
    componentContext: ComponentContext,
    private val onClosePressed:() -> Unit,
    private val onChooseFlower:() -> Unit,
    private val onAddressClicked:() -> Unit,
    private val orderId: Long
) : FillDetailsComponent, KoinComponent, ComponentContext by componentContext
{
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = fillDetailsStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferencesSetting = sharedPreferences,
        subscriptionApi = subscriptionApi
    )

    override val model: Value<Model> = store.states.toModels()


    init {
        lifecycle.doOnResume {
            if (orderId.toInt() != 0) {
                store.accept(intent = Intent.LoadOrderDetails(orderId = orderId))
            }
        }
    }

    private fun Value<State>.toModels(): Value<Model> = map { state ->
        Model(
            orderDetails = state.orderDetails,
            orderDetailsLoaded = state.detailsLoaded
        )
    }

    override fun chooseFlower() {
        onChooseFlower()
    }

    override fun addressClicked() {
        onAddressClicked()
    }

    override fun onClose() {
        onClosePressed()
    }
}