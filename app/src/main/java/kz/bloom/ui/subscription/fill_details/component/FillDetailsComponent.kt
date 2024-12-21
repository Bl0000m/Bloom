package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.value.Value

interface FillDetailsComponent {
    data class Model(
        val bouquet: Boolean
    )

    val model: Value<Model>

    fun chooseFlower()

    fun onClose()
}