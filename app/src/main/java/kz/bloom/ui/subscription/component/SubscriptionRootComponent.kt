package kz.bloom.ui.subscription.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent

interface SubscriptionRootComponent {

    data class Model(
        val subscriptionName: String
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
        public data class FillDetails(
            public val component: FillDetailsComponent
        ) : Child
    }

    public val childStack: Value<ChildStack<*, Child>>
}