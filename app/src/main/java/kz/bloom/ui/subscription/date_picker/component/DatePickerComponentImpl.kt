package kz.bloom.ui.subscription.date_picker.component

import android.annotation.SuppressLint
import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kz.bloom.ui.subscription.api.SubscriptionApi
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.Model
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.TimeOfDay
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.DateItemDetailed
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.Intent
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class DatePickerComponentImpl(
    componentContext: ComponentContext,
    private val onContinuePressed: (selection: List<DateItem>) -> Unit,
    private val onBackPress: () -> Unit,
    private val subscriptionName: String,
    private val subscriptionTypeId: String
) : DatePickerComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    val store = DatePickerStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferences = sharedPreferences,
        subscriptionApi = subscriptionApi
    )

    private val _model = MutableValue(
        initialValue = Model(
            pickedDates = emptyList(),
            timeOfDayItems = emptyList()
        )
    )

    override val model: Value<Model> = _model

    init {
        _model.update { it.copy(timeOfDayItems = listOf(TimeOfDay.FIRST_PART, TimeOfDay.SECOND_PART, TimeOfDay.LAST_PART)) }
    }

    override fun pickADate(selection: List<LocalDate>) {
        _model.update { it.copy(pickedDates = selection.toSerializedClass()) }
    }

    override fun onContinue(selection: List<LocalDate>) {
        _model.update { it.copy(pickedDates = selection.toSerializedClass()) }
        store.accept(
            intent = Intent.CreateSubscription(
                userId = sharedPreferences.userId!!,
                subscriptionName = subscriptionName,
                subscriptionTypeId = subscriptionTypeId,
                dates = _model.value.pickedDates.toDetailedDateItems(_model.value.selectedTimeOfDay!!)
            )
        )
        onContinuePressed(selection.toSerializedClass())
    }

    override fun onBackPressed() {
        onBackPress()
    }

    override fun onTimeOfDaySelected(timeOfDay: TimeOfDay) {
        _model.update { it.copy(selectedTimeOfDay = timeOfDay) }
    }
}
@SuppressLint("NewApi")
fun List<DateItem>.toDetailedDateItems(selectedTimeOfDay: TimeOfDay): List<DateItemDetailed> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    fun getTimeRange(timeOfDay: TimeOfDay): Pair<String, String> {
        return when (timeOfDay) {
            TimeOfDay.FIRST_PART -> "08:00" to "12:59"
            TimeOfDay.SECOND_PART -> "13:00" to "17:59"
            TimeOfDay.LAST_PART -> "18:00" to "22:59"
        }
    }
    val (startTime, endTime) = getTimeRange(selectedTimeOfDay)

    return this.map { dateItem ->
        val localDate = LocalDate.of(
            dateItem.year,
            monthNameToNumber(dateItem.month),
            dateItem.dayOfMonth
        )
        DateItemDetailed(
            orderDate = localDate.format(formatter),
            orderStartTime = startTime,
            orderEndTime = endTime
        )
    }
}

fun monthNameToNumber(month: String): Int {
    return when (month.lowercase()) {
        "января" -> 1
        "февраля" -> 2
        "марта" -> 3
        "апреля" -> 4
        "мая" -> 5
        "июня" -> 6
        "июля" -> 7
        "августа" -> 8
        "сентября" -> 9
        "октября" -> 10
        "ноября" -> 11
        "декабря" -> 12
        else -> throw IllegalArgumentException("Некорректное название месяца: $month")
    }
}


@SuppressLint("NewApi")
fun List<LocalDate>.toSerializedClass(): List<DateItem> {
    val locale = Locale("ru")
    return this.map { localDate ->
        DateItem(
            dayOfMonth = localDate.dayOfMonth,
            month = localDate.month.getDisplayName(TextStyle.FULL, locale),
            dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, locale),
            year = LocalDate.now().year
        )
    }
}
