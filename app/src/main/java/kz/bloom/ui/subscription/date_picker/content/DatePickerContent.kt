package kz.bloom.ui.subscription.date_picker.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kz.bloom.R
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.TimeOfDay
import kz.bloom.ui.ui_components.custom_calendar_components.CustomDay
import kz.bloom.ui.ui_components.custom_calendar_components.CustomMonthHeader

@Composable
fun DatePickerContent(modifier: Modifier = Modifier, component: DatePickerComponent) {
    val model = component.model.subscribeAsState()
    
    Box(modifier = modifier) {
        Column(modifier = Modifier
            .padding(top = 42.dp)
            .padding(horizontal = 21.dp)) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_back_black), contentDescription = null)
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
            Row(modifier = Modifier.padding(top = 35.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TimeOfDayItem(timeOfDay = TimeOfDay.FIRST_PART)
                TimeOfDayItem(timeOfDay = TimeOfDay.SECOND_PART)
                TimeOfDayItem(timeOfDay = TimeOfDay.LAST_PART)
            }

            SelectableCalendar(
                modifier = Modifier.padding(top = 35.dp),
                showAdjacentMonths = false,
                calendarState = rememberSelectableCalendarState(
                    initialSelectionMode = SelectionMode.Multiple
                ),
                dayContent = { CustomDay(state = it) },
                monthHeader = { CustomMonthHeader(monthState = it) }
            )
        }
    }
}

//@Composable
//fun CalendarView(modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//    ) {
//        // Верхняя панель с названием месяца и кнопками
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(
//                modifier = Modifier.size(size = 21.dp),
//                onClick = { /* Обработка клика на кнопку назад */ }) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_expand_left_light_21),
//                    contentDescription = null
//                )
//            }
//            Text(
//                text = "АВГУСТ",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.align(Alignment.CenterVertically)
//            )
//            IconButton(
//                modifier = Modifier.size(21.dp),
//                onClick = { /* Обработка клика на кнопку вперед */ },
//            ) {
//                Icon(
//                    modifier = Modifier.rotate(180f),
//                    painter = painterResource(id = R.drawable.ic_expand_left_light_21),
//                    contentDescription = null
//                )
//            }
//        }
//
//        HorizontalDivider()
//
//        // Заголовки дней недели
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
//            horizontalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС").forEach { day ->
//                Text(
//                    text = day,
//                    style = MaterialTheme.typography.labelSmall.copy(lineHeight = 24.sp),
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Сетка дней месяца
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(7),
//            modifier = Modifier.fillMaxWidth(),
//            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
//        ) {
//            // Заполняем пустыми элементами для смещения начала месяца
//            items(1) { Spacer(modifier = Modifier) }
//
//            // Заполняем числа месяца
//            items(31) { day ->
//                Box(
//                    modifier = Modifier
//                        .aspectRatio(1f)
//                        .padding(4.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        modifier = Modifier.align(Alignment.Center),
//                        text = (day + 1).toString(),
//                        style = MaterialTheme.typography.bodySmall,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
private fun TimeOfDayItem(modifier: Modifier = Modifier, timeOfDay: TimeOfDay) {
    var timeText by remember { mutableStateOf(value = "") }
    timeText = when(timeOfDay) {
        TimeOfDay.FIRST_PART -> { "с 8:00 до 12:59" }
        TimeOfDay.SECOND_PART -> { "с 13:00 до 17:59" }
        TimeOfDay.LAST_PART -> { "с 18:00 до 22:59" }
    }
    Box(modifier = modifier
        .height(25.dp)
        .border(width = 0.5.dp, color = Color.Black)) {
        Text(
            modifier = Modifier
                .padding(vertical = 5.5.dp, horizontal = 10.dp)
                .align(Alignment.Center),
            text = timeText,
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.sp)
        )
    }
}