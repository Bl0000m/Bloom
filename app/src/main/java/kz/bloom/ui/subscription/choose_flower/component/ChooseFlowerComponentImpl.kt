package kz.bloom.ui.subscription.choose_flower.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent.Model

class ChooseFlowerComponentImpl(
    componentContext: ComponentContext
) : ChooseFlowerComponent, KoinComponent, ComponentContext by componentContext {
    private val _model = MutableValue(
        initialValue = Model(
            flowers = emptyList()
        )
    )
    override val model: Value<Model> = _model
}