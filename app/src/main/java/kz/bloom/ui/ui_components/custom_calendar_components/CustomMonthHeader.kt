package kz.bloom.ui.ui_components.custom_calendar_components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.header.MonthState
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import kz.bloom.R

@Composable
@SuppressLint("NewApi")
public fun CustomMonthHeader(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selectedMonth: Month
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DecrementButton(monthState = monthState)
            Text(
                modifier = Modifier.clickable { onClick() },
                text = selectedMonth.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale.getDefault()
                ).uppercase(),
                style = MaterialTheme.typography.bodyLarge,
            )
            IncrementButton(monthState = monthState)
        }
        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
    }
}

@SuppressLint("NewApi")
@Composable
private fun DecrementButton(
    monthState: MonthState,
) {
    IconButton(
        modifier = Modifier.testTag("Decrement"),
        enabled = monthState.currentMonth > monthState.minMonth,
        onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_expand_left_light_21),
            contentDescription = null
        )
    }
}

@SuppressLint("NewApi")
@Composable
private fun IncrementButton(
    monthState: MonthState,
) {
    IconButton(
        modifier = Modifier.testTag("Increment"),
        enabled = monthState.currentMonth < monthState.maxMonth,
        onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
    ) {
        Icon(
            modifier = Modifier.rotate(180f),
            painter = painterResource(id = R.drawable.ic_expand_left_light_21),
            contentDescription = null
        )
    }
}