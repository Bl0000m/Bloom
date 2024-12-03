package kz.bloom.ui.subscription.date_picker.component

import com.arkivanov.decompose.value.Value
import java.time.LocalDate
import kotlinx.serialization.Serializable

interface DatePickerComponent {
    data class Model(
        val pickedDates: List<DateItem>
    )

    @Serializable
    data class DateItem(
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val month: String,
    )

    enum class TimeOfDay {
        FIRST_PART,
        SECOND_PART,
        LAST_PART
    }

    val model: Value<Model>

    fun onContinue(selection: List<LocalDate>)

    fun onBackPressed()
}