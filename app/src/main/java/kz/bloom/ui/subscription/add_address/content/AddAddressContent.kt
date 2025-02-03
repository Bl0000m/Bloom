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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.auth.country_chooser.component.isRussiaOrKazakhstan
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PhoneNumberMaskVisualTransformation
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
                onValueChange = { component.onHouseFill(house = it) },
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
            value = model.value.floor,
            onValueChange = {
                component.onFloorFill(it)
            },
            label = "ЭТАЖ",
            placeholder = "",
            labelStyle = MaterialTheme.typography.bodySmall,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Row(modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { component.openCountryChooser() }) {
                    Text(
                        text = model.value.selectedCountry.dialCode,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        modifier = Modifier.padding(start = 10.dp),
                        painter = painterResource(id = R.drawable.ic_expand_left_light),
                        contentDescription = null
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .width(50.dp)
                        .padding(top = 15.dp),
                    thickness = 0.5.dp,
                    color = Color.Black
                )
            }

            LabeledTextField(
                label = "НОМЕР ТЕЛЕФОНА",
                singleLine = true,
                placeholder = null,
                trailingContent = {
                    if (model.value.recipientPhoneNumber.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable { component.clearPhone() },
                            painter = painterResource(id = R.drawable.ic_close_round_fill_light),
                            contentDescription = null
                        )
                    }
                },
                labelStyle = MaterialTheme.typography.labelSmall,
                onValueChange = { component.onPhoneNumberFill(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = model.value.recipientPhoneNumber,
                visualTransformation = if (model.value.selectedCountry.isRussiaOrKazakhstan) {
                    PhoneNumberMaskVisualTransformation(length = 10)
                } else VisualTransformation.None
            )
        }

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
            },
            isEnabled = model.value.isPrimaryButtonEnabled
        )
    }
}