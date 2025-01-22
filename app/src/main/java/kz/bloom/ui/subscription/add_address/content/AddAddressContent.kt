package kz.bloom.ui.subscription.add_address.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun AddAddressContent(modifier: Modifier = Modifier, component: AddAddressComponent) {

    val model = component.model.subscribeAsState()

    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)
        .padding(horizontal = 21.dp)
    ) {
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
        Spacer(modifier = Modifier.height(33.dp))
        LabeledTextField(
            modifier = Modifier.padding(top = 4.dp),
            value = model.value.city,
            onValueChange = { },
            label = "ГОРОД",
            labelStyle = MaterialTheme.typography.bodySmall,
            singleLine = true,
            placeholder = ""
        )
        LabeledTextField(
            modifier = Modifier.padding(top = 4.dp),
            value = model.value.street,
            onValueChange = { component.onStreetFill(it)},
            labelStyle = MaterialTheme.typography.bodySmall,
            label = "УЛИЦА",
            placeholder = ""
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LabeledTextField(
                modifier = Modifier
                    .padding(top = 4.dp),
                value = model.value.house,
                onValueChange = { component.onHouseFill(house = it)},
                label = "ДОМ/ЗДАНИЕ",
                placeholder = "",
                singleLine = true,
                labelStyle = MaterialTheme.typography.bodySmall,
                isHalf = true

            )
            LabeledTextField(
                modifier = Modifier
                    .padding(top = 4.dp),
                value = model.value.apartment,
                onValueChange = { component.onApartmentFill(it) },
                label = "КВАРТИРА/ОФИС",
                placeholder = "",
                isHalf = true
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LabeledTextField(
                modifier = Modifier
                    .width(167.dp)
                    .padding(top = 4.dp),
                value = model.value.entry,
                onValueChange = { component.onEntryFill(it) },
                label = "ПОДЪЕЗД",
                placeholder = "",
                isHalf = true
            )
            LabeledTextField(
                modifier = Modifier
                    .width(167.dp)
                    .padding(top = 4.dp),
                value = model.value.intercom,
                onValueChange = {
                    component.onIntercomFill(it)
                },
                label = "ДОМОФОН",
                placeholder = "",
                isHalf = true
            )
        }
        LabeledTextField(
            modifier = Modifier.padding(top = 4.dp),
            value = model.value.recipientPhoneNumber,
            onValueChange = {
                component.onFloorFill(it)
            },
            label = "ЭТАЖ",
            placeholder = "",
            labelStyle = MaterialTheme.typography.bodySmall,

        )
        LabeledTextField(
            modifier = Modifier.padding(top = 4.dp),
            value = model.value.comment,
            labelStyle = MaterialTheme.typography.bodySmall,
            onValueChange = {
                component.onCommentFill(it)
            },
            label = "КОММЕНТАРИЙ К ДОСТАВКЕ",
            placeholder = ""
        )
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "ПОДТВЕРДИТЬ",
            onClick = {
                component.createAddress()
            }
        )
    }
}