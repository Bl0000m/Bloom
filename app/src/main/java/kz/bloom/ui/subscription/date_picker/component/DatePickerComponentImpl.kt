package kz.bloom.ui.subscription.date_picker.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.Model
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem

class DatePickerComponentImpl(
    componentContext: ComponentContext,
    private val onContinuePressed:() -> Unit,
    private val onBackPress:() -> Unit
) : DatePickerComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            pickedDatesLimit = 30,
            pickedDates = emptyList()
        )
    )

    override val model: Value<Model> = _model

    override fun chooseDay() {

    }

    override fun onContinue() {
        onContinuePressed()
    }

    override fun onBackPressed() {
        onBackPress()
    }
}