package kz.bloom.ui.subscription.fill_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent

@Composable
fun FillDetailsContent(modifier: Modifier = Modifier, component: FillDetailsComponent) {

    val model = component.model.subscribeAsState()

    val showDialog = remember {
        mutableStateOf(true)
    }
    if (showDialog.value) {
        UserInformingDialog(
            onDismiss = { showDialog.value = false }
        )
    }
    Column {
        Column(modifier = modifier.padding(horizontal = 21.dp).padding(top = 8.dp)) {
            Icon(
                modifier = Modifier.clickable { component.onNavigateToRightBack() },
                painter = painterResource(id = R.drawable.ic_close_square_light),
                contentDescription = null)
            Spacer(modifier = Modifier.height(41.dp))
            Text(
                text = "ЗАПОЛНИТЕ ДЕТАЛИ ЗАКАЗА",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "ВСЕГО ЗАКАЗОВ: ${model.value.selection.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        HorizontalDivider(color = Color.Black)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                count = model.value.selection.size,
                key = {
                    index -> val dateItem = model.value.selection[index]
                    "${dateItem.dayOfMonth}-${dateItem.dayOfWeek}-${dateItem.month}"
                }
            ) { index ->
                val date = model.value.selection[index]
                IndividualOrderPanels(day = date.dayOfMonth, month = date.month.uppercase(), dayOfWeek = date.dayOfWeek.uppercase())
            }
        }
    }
}
@Composable
private fun IndividualOrderPanels(
    modifier: Modifier = Modifier,
    day: Int,
    month: String,
    dayOfWeek: String) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.padding(start = 21.dp, end = 19.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .clickable {  }
                .border(width = 0.5.dp, color = Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.padding(vertical = 13.dp, horizontal = 38.dp),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "$day $month, $dayOfWeek",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp)
                )
                Text(
                    text = "Уточните детали заказа.",
                    color = Color(0xFFCCCCCC),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = Color.Black)
    }
}

@Composable
private fun UserInformingDialog(
    modifier: Modifier = Modifier,
    onDismiss:() -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.5F))
        .blur(20.dp)
    ) {
        Popup(
            onDismissRequest = { onDismiss() },
            alignment = Alignment.Center
        ) {
            Box(modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .border(width = 0.5.dp, color = Color.Black)
                .background(color = Color.White)
            ) {
                Column(modifier = Modifier.padding(top = 20.dp, start = 18.dp, bottom = 10.dp)) {
                    Row(modifier = Modifier.padding(end = 12.dp)) {
                        Text(
                            text = "ЗАПОЛНИТЕ ДЕТАЛИ ЗАКАЗА",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(painter = painterResource(id = R.drawable.ic_close_square_light), contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Пожалуйста, укажите все необходимые данные \nдля оформления, чтобы мы могли обработать \nваш заказ без задержек. Точные и полные\n детали помогут обеспечить своевременную \nдоставку.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(56.dp))
                }
                Box(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onDismiss() }
                    .background(color = Color.Black)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 46.dp),
                        text = "ПОДТВЕРДИТЬ",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}