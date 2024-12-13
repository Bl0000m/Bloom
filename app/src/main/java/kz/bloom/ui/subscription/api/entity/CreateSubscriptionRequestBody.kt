package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.DateItemDetailed


@Serializable
data class CreateSubscriptionRequestBody(
    val userId: Long,
    val name: String,
    val subscriptionTypeId: Long,
    val orderDates: List<DateItemDetailed>
)