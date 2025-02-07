package kz.bloom.ui.subscription.address_list.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.address_list.component.AddressListComponent
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun AddressListContent(modifier: Modifier = Modifier, component: AddressListComponent) {
    val model = component.model.subscribeAsState()
    Column(modifier = modifier.padding(horizontal = 21.dp)) {
        Icon(
            modifier = Modifier
                .clickable { component.navigateBack() }
                .padding(top = 42.dp),
            painter = painterResource(id = R.drawable.ic_arrow_back_black),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(height = 33.dp))
        Text(
            text = "АДРЕСС ДОСТАВКИ",
            style = MaterialTheme.typography.bodyLarge
        )
        if (model.value.addressList.isEmpty()) {
            Spacer(modifier = Modifier.height(92.dp))
            NoSavedAddressesContent()
            HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
        } else {
            Spacer(modifier = Modifier.height(92.dp))
            model.value.addressList.forEach { addressItem ->
                AddressItem(addressItem = addressItem)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "Указать другой адресс",
            onClick = { component.addAddress() }
        )
    }
}

@Composable
private fun AddressItem(modifier: Modifier = Modifier, addressItem: AddressListComponent.Address) {
    Row(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = addressItem.street.uppercase(),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = addressItem.city,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_meatballs_menu),
            contentDescription = null
        )
    }
}

@Composable
private fun NoSavedAddressesContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_geo_pin),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "У ВАС ПОКА НЕТ СОХРАНЕННЫХ АДРЕСОВ",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Как только появятся новые адреса, они будут \nотображаться здесь.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}