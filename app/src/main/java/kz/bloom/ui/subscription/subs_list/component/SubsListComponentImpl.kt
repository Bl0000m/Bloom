package kz.bloom.ui.subscription.subs_list.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent.Model

class SubsListComponentImpl(
    componentContext: ComponentContext,
    private val onChooseDate: () -> Unit
) : SubsListComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            subsList = emptyList()
        )
    )
    override val model: Value<Model> = _model

    override fun chooseDate() {
        onChooseDate()
    }
}