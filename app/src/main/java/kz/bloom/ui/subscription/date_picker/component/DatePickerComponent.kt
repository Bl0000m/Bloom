package kz.bloom.ui.subscription.date_picker.component

import com.arkivanov.decompose.value.Value
import java.time.DayOfWeek
import java.time.Month

interface DatePickerComponent {
    data class Model(
        val pickedDates: List<DateItem>,
        val pickedDatesLimit: Int
    )

    data class DateItem(
        val dayOfWeek: DayOfWeek,
        val month: Month,
        val dayOfMonth: Int
    )

    enum class TimeOfDay {
        FIRST_PART,
        SECOND_PART,
        LAST_PART
    }

    val model: Value<Model>

    fun chooseDay()

    fun onContinue()
}