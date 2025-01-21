package kz.bloom.ui.subscription.address_list.content

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kz.bloom.ui.subscription.address_list.component.AddressListComponent
import kz.bloom.ui.ui_components.PrimaryButton

@Composable
fun AddressListContent(modifier: Modifier = Modifier, component: AddressListComponent) {

    Column {
        PrimaryButton(text = "Указать другой адресс", onClick = { component.addAddress() })

    }
}