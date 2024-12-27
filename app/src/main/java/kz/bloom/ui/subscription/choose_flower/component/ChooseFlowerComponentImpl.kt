package kz.bloom.ui.subscription.choose_flower.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kz.bloom.libraries.states
import kz.bloom.ui.subscription.api.SubscriptionApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent.Model
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent.Event
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.choose_flower.store.chooseFlowerStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.State
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.Intent
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto
import kz.bloom.ui.ui_components.coroutineScope

import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class ChooseFlowerComponentImpl(
    componentContext: ComponentContext,
    private val onCloseBouquet: () -> Unit
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

    private val _events = MutableSharedFlow<Event>(replay = 0)

    override val events: Flow<Event> = _events

    private fun Value<State>.toModels(): Value<Model> = map { state ->
        Model(
            bouquetsInfo = state.bouquets,
            bouquetPhotos = state.bouquets.toBouquetPhotos(),
            bouquetDetails = state.bouquetDetails
        )
    }

    override fun flowerConsidered(bouquetDTO: BouquetDTO) {
        store.accept(intent = Intent.LoadSpecificBouquet(bouquetDTO.id))
        store.states.subscribe { state ->
            scope.launch {
                if (state.bouquetDetails != null) {
                    _events.emit(value = Event.DisplayBouquetDetails(state.bouquetDetails))
                }
            }
        }
    }

    override fun closeBouquet() {
        onCloseBouquet()
    }

    private fun List<BouquetDTO>.toBouquetPhotos(): List<BouquetPhoto> {
        return this.flatMap { bouquetDTO ->
            bouquetDTO.bouquetPhotos.map { photo ->
                BouquetPhoto(id = photo.id, url = photo.url)
            }
        }
    }
}