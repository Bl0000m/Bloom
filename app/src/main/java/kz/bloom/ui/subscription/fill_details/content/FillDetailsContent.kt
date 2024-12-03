package kz.bloom.ui.subscription.fill_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kz.bloom.R
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent

@Composable
fun FillDetailsContent(modifier: Modifier = Modifier, component: FillDetailsComponent) {
    val showDialog = remember {
        mutableStateOf(true)
    }
    if (showDialog.value) {
        UserInformingDialog(
            onDismiss = { showDialog.value = false }
        )
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