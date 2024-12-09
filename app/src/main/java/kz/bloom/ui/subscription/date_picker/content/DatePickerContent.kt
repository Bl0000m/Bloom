package kz.bloom.ui.subscription.date_picker.content

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kz.bloom.R
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.TimeOfDay
import kz.bloom.ui.ui_components.PrimaryButton
import kz.bloom.ui.ui_components.custom_calendar_components.CustomDay
import kz.bloom.ui.ui_components.custom_calendar_components.CustomDayOfWeekHeader
import kz.bloom.ui.ui_components.custom_calendar_components.CustomMonthHeader

@SuppressLint("NewApi")
@Composable
fun DatePickerContent(modifier: Modifier = Modifier, component: DatePickerComponent) {
    val showMonthPicker = remember { mutableStateOf(false) }
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }

    val model = component.model.subscribeAsState()

    val monthState = remember {
        MonthState(
            initialMonth = currentMonth.value,
            minMonth = YearMonth.now().minusYears(1),
            maxMonth = YearMonth.now().plusYears(5)
        )
    }

    val selectionState = remember {
        DynamicSelectionState(
            selection = emptyList(),
            selectionMode = SelectionMode.Multiple
        )
    }
    var isPrimaryBTNEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(model.value.selectedTimeOfDay, model.value.pickedDates) {
        isPrimaryBTNEnabled = model.value.selectedTimeOfDay != null && model.value.pickedDates.isNotEmpty()
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 42.dp)
                .padding(horizontal = 21.dp)
        ) {
            Icon(
                modifier = Modifier.clickable { component.onBackPressed() },
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "ВЫБЕРИТЕ КОЛИЧЕСТВО ДНЕЙ"
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Выберите даты на календаре, и мы доставим \nцветы в точно выбранные вами дни"
                )
            }
            Row(
                modifier = Modifier.padding(top = 35.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                model.value.timeOfDayItems.forEach { timeOfDay ->
                    TimeOfDayItem(
                        timeOfDay = timeOfDay,
                        isSelected = model.value.selectedTimeOfDay == timeOfDay,
                        onClick = { selectedTime ->
                            component.onTimeOfDaySelected(timeOfDay = selectedTime)
                        }
                    )
                }
            }
            SelectableCalendar(
                modifier = Modifier.padding(top = 35.dp),
                showAdjacentMonths = false,
                calendarState = rememberSelectableCalendarState(
                    initialSelectionMode = SelectionMode.Multiple,
                    monthState = monthState
                ),
                dayContent = {
                    CustomDay(
                        dayState = it,
                        onClick = {
                            date -> selectionState.onDateSelected(date)
                            component.pickADate(selection = selectionState.selection)
                        }
                    )
                },
                monthHeader = {
                    CustomMonthHeader(
                        monthState = monthState,
                        onClick = {
                            showMonthPicker.value = true
                        },
                        selectedMonth = monthState.currentMonth.month
                    )
                },
                daysOfWeekHeader = { CustomDayOfWeekHeader(daysOfWeek = it) }
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier.padding(bottom = 21.dp),
                text = "ПРОДОЛЖИТЬ",
                isEnabled = isPrimaryBTNEnabled,
                textStyle = MaterialTheme.typography.bodySmall,
                onClick = { component.onContinue(selectionState.selection) }
            )
        }
        if (showMonthPicker.value) {
            CustomMonthSelectionDialog(
                onMonthSelected = { selectedYearMonth ->
                    showMonthPicker.value = false
                    currentMonth.value = selectedYearMonth
                    monthState.currentMonth = selectedYearMonth
                },
                onDismiss = { showMonthPicker.value = false },
                selectedMonth = monthState.currentMonth
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun CustomMonthSelectionDialog(
    selectedMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .blur(20.dp)
    ) {
        Popup(
            onDismissRequest = { onDismiss() },
            alignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 21.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = RectangleShape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ВЫБЕРИТЕ МЕСЯЦ",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onDismiss() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close_square_light),
                                contentDescription = "Закрыть"
                            )
                        }
                    }
                    MonthRow(
                        months = Month.entries,
                        selectedMonth = selectedMonth,
                        onMonthSelected = onMonthSelected,
                        fromIndex = 0,
                        toIndex = 4
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    MonthRow(
                        months = Month.entries,
                        selectedMonth = selectedMonth,
                        onMonthSelected = onMonthSelected,
                        fromIndex = 4,
                        toIndex = 8
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    MonthRow(
                        months = Month.entries,
                        selectedMonth = selectedMonth,
                        onMonthSelected = onMonthSelected,
                        fromIndex = 8,
                        toIndex = 12
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeOfDayItem(
    modifier: Modifier = Modifier,
    timeOfDay: TimeOfDay,
    isSelected: Boolean,
    onClick: (TimeOfDay) -> Unit
) {
    var timeText by remember { mutableStateOf(value = "") }
    timeText = when (timeOfDay) {
        TimeOfDay.FIRST_PART -> {
            "8:00 - 12:59"
        }

        TimeOfDay.SECOND_PART -> {
            "13:00 - 17:59"
        }

        TimeOfDay.LAST_PART -> {
            "18:00 - 22:59"
        }
    }
    Box(
        modifier = modifier
            .height(25.dp)
            .border(width = 0.5.dp, color = Color.Black)
            .clickable { onClick(timeOfDay) }
            .background(color = if (isSelected) Color.Black else MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 5.5.dp, horizontal = 10.dp)
                .align(Alignment.Center),
            text = timeText,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.sp)
        )
    }
}

@SuppressLint("NewApi")
@Composable
fun MonthRow(
    months: List<Month>,
    selectedMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit,
    fromIndex: Int,
    toIndex: Int
) {
    Row(
        modifier = Modifier.height(25.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        months.subList(fromIndex, toIndex).forEach { month ->
            val isSelected = month == selectedMonth.month
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { onMonthSelected(YearMonth.of(selectedMonth.year, month)) }
                    .clip(shape = RectangleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                    .border(width = 0.5.dp, color = Color.Black)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 5.5.dp, horizontal = 10.dp),
                    text = month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale.getDefault()
                    ).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}