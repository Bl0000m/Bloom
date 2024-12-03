package kz.bloom.ui.subscription.create_subscription.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface CreateSubscriptionComponent {
    data class Model(
        val subscriptionName: String,
        val pickedSubscriptionName: String,
        val subscriptionTypeError: String,
        val subscriptionNameErrorText: String,
        val subscriptionType: List<SubType>
    )

    data class SubType(
        val number: Int,
        val name: String
    )

    sealed interface Event {
        data object ShowSubscriptionTypeSheet : Event
    }

    val model: Value<Model>

    val events: Flow<Event>

    fun createSub()

    fun onNavigateBack()

    fun fillSubName(name: String)

    fun chosenComposition(subName: String)
}