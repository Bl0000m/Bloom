package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent.Model
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem

class FillDetailsComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateRightBack:() -> Unit,
    selection: List<DateItem>
) : FillDetailsComponent, KoinComponent, ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            selection = selection
        )
    )

    override val model: Value<Model> = _model

    override fun onNavigateToRightBack() {
        onNavigateRightBack()
    }
}