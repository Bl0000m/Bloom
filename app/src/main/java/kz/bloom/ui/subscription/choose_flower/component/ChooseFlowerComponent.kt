package kz.bloom.ui.subscription.choose_flower.component

import com.arkivanov.decompose.value.Value

interface ChooseFlowerComponent {
    data class Model(
        val flowers: List<FlowerItem>
    )

    val model: Value<Model>

    data class FlowerItem(
        val imageRes: Int,
        val title: String,
        val price: String
    )
}