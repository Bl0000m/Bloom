package kz.bloom.ui.subscription.fill_details.content

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun FillDetailsContent(modifier: Modifier = Modifier, component: FillDetailsComponent) {
    val model = component.model.subscribeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close_square_light),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(41.dp))
        Text(
            text = "ДЕТАЛИ ЗАКАЗА",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(35.dp))

        if (model.value.orderDetailsLoaded) {
            val orderDetails = model.value.orderDetails
            FilledBouquetContent(
                orderCode = orderDetails.orderCode.toString(),
                branchName = orderDetails.branchDivisionInfoDto.divisionType,
                bouquetName = orderDetails.bouquetInfo!!.name,
                price = orderDetails.assemblyCost.toString(),
                status = orderDetails.orderStatus,
                bouquetPhotoUrl = orderDetails.bouquetInfo.bouquetPhotos.first().url
            )
        } else {
            Text(
                text = "БУКЕТ",
                style = MaterialTheme.typography.bodySmall
            )
            DetailContent(
                icon = R.drawable.ic_flower,
                actionText = "ВЫБЕРИТЕ БУКЕТ",
                onClick = { component.chooseFlower() }
            )
        }
        Text(
            text = "ДАТА ДОСТАВКИ",
            style = MaterialTheme.typography.bodySmall
        )
        DetailContent(
            icon = R.drawable.ic_clock,
            actionText = "ВЫБЕРИТЕ ВРЕМЯ И ДАТУ ДОСТАВКИ",
            onClick = { }
        )
        Text(
            text = "АДРЕС ДОСТАВКИ",
            style = MaterialTheme.typography.bodySmall
        )
        DetailContent(
            icon = R.drawable.ic_geo_pin,
            actionText = "ВЫБЕРИТЕ АДРЕС ДОСТАВКИ",
            onClick = { component.onAddressClicked() }
        )
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier.padding(bottom = 21.dp),
            text = "ПРОДОЛЖИТЬ",
            onClick = { }
        )

    }
}

@Composable
fun FilledBouquetContent(
    modifier: Modifier = Modifier,
    orderCode: String,
    bouquetName: String,
    branchName: String,
    price: String,
    status: String,
    bouquetPhotoUrl: String
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 35.dp)) {
        HorizontalDivider(thickness = 0.5.dp)
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(292.dp)
                        .width(194.dp),
                    model = bouquetPhotoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 84.dp)
                ) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_square_light),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { /* Handle close */ }
                        )
                    }
                    Text(
                        text = orderCode,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = bouquetName,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = branchName,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        HorizontalDivider(thickness = 0.5.dp)
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    icon: Int,
    actionText: String,
    onClick: () -> Unit
) {
    Column(modifier = modifier
        .padding(top = 20.dp, bottom = 35.dp)
        .clickable { onClick() }) {
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
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_right_24),
                contentDescription = null
            )
        }
    }
}