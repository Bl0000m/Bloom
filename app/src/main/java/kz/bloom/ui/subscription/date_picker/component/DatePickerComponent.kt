package kz.bloom.ui.subscription.date_picker.component

import com.arkivanov.decompose.value.Value
import java.time.LocalDate
import kotlinx.serialization.Serializable

interface DatePickerComponent {
    data class Model(
        val pickedDates: List<DateItem>,
        val timeOfDayItems: List<TimeOfDay>,
        val selectedTimeOfDay: TimeOfDay? = null
    )

    @Serializable
    data class DateItem(
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val month: String,
        val year: Int
    )

    enum class TimeOfDay {
        FIRST_PART,
        SECOND_PART,
        LAST_PART
    }

    val model: Value<Model>

    fun onContinue(selection: List<LocalDate>)

    fun onBackPressed()

    fun pickADate(selection: List<LocalDate>)

    fun onTimeOfDaySelected(timeOfDay: TimeOfDay)
}