package kz.bloom.ui.subscription.flower_details.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto

interface FlowerDetailsComponent {
    data class Model(
        val bouquetDetails: BouquetDetailsResponse,
        val bouquetPhotos: List<BouquetPhoto>,
        val bouquetPrice: String
    )

    val model: Value<Model>

    fun closeDetails()

    fun pickBouquet(bouquetId: Long)
}