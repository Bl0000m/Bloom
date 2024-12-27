package kz.bloom.ui.subscription.choose_flower.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto

interface ChooseFlowerComponent {
    data class Model(
        val bouquetsInfo: List<BouquetDTO>,
        val bouquetPhotos: List<BouquetPhoto>,
        val bouquetDetails: BouquetDetailsResponse? = null
    )

    public sealed interface Event {
        public data class DisplayBouquetDetails(
            public val bouquet: BouquetDetailsResponse
        ) : Event
    }

    val model: Value<Model>

    val events: Flow<Event>

    fun flowerConsidered(bouquetDTO: BouquetDTO)

    fun closeBouquet()
}