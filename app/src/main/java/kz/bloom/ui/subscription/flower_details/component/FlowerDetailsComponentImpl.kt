package kz.bloom.ui.subscription.flower_details.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.flower_details.component.FlowerDetailsComponent.Model
import kz.bloom.ui.subscription.flower_details.store.flowerDetailsStore
import kz.bloom.ui.subscription.flower_details.store.FlowerDetailsStore.State
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class FlowerDetailsComponentImpl(
    componentContext: ComponentContext,
    private val onCloseDetails:() -> Unit,
    private val onBouquetPicked:(bouquetId: Long) -> Unit,
    bouquetDTO: BouquetDTO
): FlowerDetailsComponent, KoinComponent, ComponentContext by componentContext {
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = flowerDetailsStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferences = sharedPreferences,
        subscriptionApi = subscriptionApi,
        bouquetDTO = bouquetDTO
    )
    override val model: Value<Model> = store.states.toModels(bouquetDTO)


    override fun closeDetails() {
        onCloseDetails()
    }

    override fun pickBouquet(bouquetId: Long) {
        onBouquetPicked(bouquetId)
    }

    private fun Value<State>.toModels(bouquetDTO: BouquetDTO): Value<Model> = map { state ->
        Model(
            bouquetDetails = state.bouquetDetailsResponse,
            bouquetPhotos = state.bouquetDetailsResponse.toBouquetPhotos(),
            bouquetPrice = bouquetDTO.price.toString()
        )
    }

    private fun BouquetDetailsResponse.toBouquetPhotos(): List<BouquetPhoto> {
        return this.bouquetPhotos.map { photo ->
            BouquetPhoto(id = photo.id, url = photo.url)
        }
    }
}