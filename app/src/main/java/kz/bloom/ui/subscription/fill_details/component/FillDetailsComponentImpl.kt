package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent.Model
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent.Bouquet

class FillDetailsComponentImpl(
    componentContext: ComponentContext,
    private val onClosePressed:() -> Unit
) : FillDetailsComponent, KoinComponent, ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            bouquet = Bouquet(
                id = 0,
                name = "",
                supplier = "",
                coast = 0.0
            )
        )
    )

    override val model: Value<Model> = _model

}