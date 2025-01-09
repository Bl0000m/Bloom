package kz.bloom.ui.subscription.choose_flower.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO

interface ChooseFlowerComponent {
    data class Model(
        val bouquetsInfo: List<BouquetDTO>
    )

    val model: Value<Model>

    fun flowerConsidered(bouquetDTO: BouquetDTO)

    fun closeBouquet()
}