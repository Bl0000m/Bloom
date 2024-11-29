package kz.bloom.ui.ui_components.custom_calendar_components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.LocalDate
import kz.bloom.ui.theme.InterTypography

@SuppressLint("NewApi")
@Composable
public fun <T : SelectionState> CustomDay(
    state: DayState<T>,
    modifier: Modifier = Modifier,
    selectionColor: Color = MaterialTheme.colorScheme.onPrimary,
    currentDayColor: Color = MaterialTheme.colorScheme.error,
    onClick: (LocalDate) -> Unit = {},
) {
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    val today = LocalDate.now()
    val isBeforeToday = date.isBefore(today)

    if (isBeforeToday) {
        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            Text(
                text = date.dayOfMonth.toString(),
                style = InterTypography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    } else {

        Box(
            modifier = modifier
                .size(32.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onClick(date)
                    selectionState.onDateSelected(date)
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .matchParentSize()
                    .background(color = if (isSelected) selectionColor else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = InterTypography.bodyMedium,
                    color = if (isSelected) Color.White else if (state.isCurrentDay) currentDayColor else Color.Black
                )
            }
        }
    }
}