package kz.bloom.ui.ui_components.custom_calendar_components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
public fun <T : SelectionState> CustomDay(
    state: DayState<T>,
    modifier: Modifier = Modifier,
    selectionColor: Color = MaterialTheme.colorScheme.primary,
    currentDayColor: Color = Color(0xFF),
    onClick: (LocalDate) -> Unit = {},
) {
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    Box(
        modifier = Modifier.clickable {
            onClick(date)
            selectionState.onDateSelected(date)
        },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = date.dayOfMonth.toString())
    }

}