package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.DateItemDetailed


@Serializable
data class CreateSubscriptionRequestBody(
    val userId: Int,
    val name: String,
    val subscriptionTypeId: Int,
    val orderDates: List<DateItemDetailed>
)