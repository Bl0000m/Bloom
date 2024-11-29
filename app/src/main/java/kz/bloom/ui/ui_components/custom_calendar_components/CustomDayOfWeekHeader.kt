package kz.bloom.ui.ui_components.custom_calendar_components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.time.DayOfWeek
import java.time.format.TextStyle.SHORT
import java.util.Locale
import kz.bloom.ui.theme.InterTypography

@SuppressLint("NewApi")
@Composable
fun CustomDayOfWeekHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek>,
) {
    Row(modifier = modifier) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(SHORT, Locale.getDefault()).uppercase(),
                style = InterTypography.labelMedium,
                modifier = modifier
                    .weight(1f)
                    .wrapContentHeight()
            )
        }
    }
}