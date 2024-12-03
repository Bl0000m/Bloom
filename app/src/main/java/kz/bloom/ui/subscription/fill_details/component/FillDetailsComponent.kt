package kz.bloom.ui.subscription.fill_details.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem

interface FillDetailsComponent {
    data class Model(
        val selection: List<DateItem>
    )

    val model: Value<Model>
}