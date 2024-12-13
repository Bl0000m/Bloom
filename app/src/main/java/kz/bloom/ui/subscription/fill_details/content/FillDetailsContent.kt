package kz.bloom.ui.subscription.fill_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.bloom.R
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun FillDetailsContent(modifier: Modifier = Modifier, component: FillDetailsComponent) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 21.dp)
            .padding(top = 8.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_close_square_light), contentDescription = null)
        Spacer(modifier = Modifier.height(41.dp))
        Text(
            text = "ДЕТАЛИ ЗАКАЗА",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "ДАТА ДОСТАВКИ",
            style = MaterialTheme.typography.bodySmall
        )
        DetailContent(icon = R.drawable.ic_clock, actionText = "ВЫБЕРИТЕ ВРЕМЯ И ДАТУ ДОСТАВКИ")
        Text(
            text = "АДРЕС ДОСТАВКИ",
            style = MaterialTheme.typography.bodySmall
        )
        DetailContent(icon = R.drawable.ic_clock, actionText = "ВЫБЕРИТЕ АДРЕС ДОСТАВКИ")
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier.padding(bottom = 21.dp),
            text = "ПРОДОЛЖИТЬ",
            onClick = { }
        )

    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    icon: Int,
    actionText: String
) {
    Column(modifier = modifier.padding(top = 20.dp, bottom = 35.dp)) {
        HorizontalDivider(thickness = 0.5.dp)
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 21.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = null)
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.ic_expand_right_24), contentDescription = null)
        }
    }
}