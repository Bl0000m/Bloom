package kz.bloom.ui.subscription.choose_flower.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.subscription.api.SubscriptionApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent.Model
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.choose_flower.store.chooseFlowerStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.State
import kz.bloom.ui.ui_components.coroutineScope

import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class ChooseFlowerComponentImpl(
    componentContext: ComponentContext,
    private val onCloseBouquet: () -> Unit,
    private val onFlowerConsidered: (bouquetDTO: BouquetDTO) -> Unit
) : ChooseFlowerComponent, KoinComponent, ComponentContext by componentContext {

    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()


    private val store = chooseFlowerStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferences = sharedPreferences,
        subscriptionApi = subscriptionApi
    )
    val scope = coroutineScope()

    override val model: Value<Model> = store.states.toModels()

    private fun Value<State>.toModels(): Value<Model> = map { state ->
        Model(
            bouquetsInfo = state.bouquets
        )
    }

    override fun flowerConsidered(bouquetDTO: BouquetDTO) {
        onFlowerConsidered(bouquetDTO)
    }

    override fun closeBouquet() {
        onCloseBouquet()
    }
}