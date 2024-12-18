package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.value.Value

interface FillDetailsComponent {
    data class Model(
        val bouquet: Bouquet
    )

    val model: Value<Model>

    data class Bouquet(
        val id: Int,
        val name: String,
        val supplier: String,
        val coast: Double
    )

    fun chooseFlower()

    fun onClose()
}