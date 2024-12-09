package kz.bloom.ui.subscription.date_picker.store

import com.arkivanov.mvikotlin.core.store.Store
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.State
import kz.bloom.ui.subscription.date_picker.store.DatePickerStore.Intent
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Serializable

interface DatePickerStore : Store<Intent, State, Nothing> {
    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val subscriptionCreated: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class CreateSubscription(
            val userId: String,
            val subscriptionName: String,
            val subscriptionTypeId: String,
            val dates: List<DateItemDetailed>
        ) : Intent
    }

    @Serializable
    data class DateItemDetailed(
        val orderDate: String,
        val orderStartTime: String,
        val orderEndTime: String
    )
}