package kz.bloom.ui.subscription.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent
import kz.bloom.ui.subscription.address_list.component.AddressListComponent
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.subscription.flower_details.component.FlowerDetailsComponent
import kz.bloom.ui.subscription.order_list.component.OrderListComponent
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent

interface SubscriptionRootComponent {

    data class Model(
        val subscriptionName: String,
        val subscriptionId: Long
    )

    val model: Value<Model>

    public sealed interface Child {
        public data class SubscriptionList(
            public val component: SubsListComponent
        ) : Child
        public data class DatePicker(
            public val component: DatePickerComponent
        ) : Child
        public data class CreateSubscription(
            public val component: CreateSubscriptionComponent
        ) : Child
        public data class OrderList(
            public val component: OrderListComponent
        ) : Child
        public data class FillDetails(
            public val component: FillDetailsComponent
        ) : Child
        public data class ChooseFlower(
            public val component: ChooseFlowerComponent
        ) : Child
        public data class FlowerDetails(
            public val component: FlowerDetailsComponent
        ) : Child
        public data class ChooseCompany(
            public val component: ChooseCompanyComponent
        ) : Child
        public data class AddressList(
            public val component: AddressListComponent
        ) : Child
        public data class AddAddress(
            public val component: AddAddressComponent
        ) : Child
        public data class CountryChoose(
            public val component: ChooseCountryComponent
        ) : Child
    }

    public val childStack: Value<ChildStack<*, Child>>
}