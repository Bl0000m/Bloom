package kz.bloom.ui.subscription.date_picker.component

import android.annotation.SuppressLint
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import java.time.LocalDate
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.Model
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem

class DatePickerComponentImpl(
    componentContext: ComponentContext,
    private val onContinuePressed:(selection: List<DateItem>) -> Unit,
    private val onBackPress:() -> Unit
) : DatePickerComponent,
    KoinComponent,
    ComponentContext by componentContext
{
    private val _model = MutableValue(
        initialValue = Model(
            pickedDates = emptyList()
        )
    )

    override val model: Value<Model> = _model

    override fun onContinue(selection: List<LocalDate>) {
        onContinuePressed(selection.toSerializedClass())
    }

    override fun onBackPressed() {
        onBackPress()
    }
}

@SuppressLint("NewApi")
fun List<LocalDate>.toSerializedClass() : List<DateItem> {
    return this.map { localDate ->
        DateItem(
            dayOfMonth = localDate.dayOfMonth,
            month = localDate.month.name,
            dayOfWeek = localDate.dayOfWeek.name
        )
    }
}
