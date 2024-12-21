package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent.Model

class FillDetailsComponentImpl(
    componentContext: ComponentContext,
    private val onClosePressed:() -> Unit,
    private val onChooseFlower:() -> Unit
) : FillDetailsComponent, KoinComponent, ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            bouquet = false
        )
    )

    override val model: Value<Model> = _model

    override fun chooseFlower() {
        onChooseFlower()
    }

    override fun onClose() {
        onClosePressed()
    }
}