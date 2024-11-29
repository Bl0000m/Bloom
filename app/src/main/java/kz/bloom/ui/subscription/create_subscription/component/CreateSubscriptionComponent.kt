package kz.bloom.ui.subscription.create_subscription.component

import com.arkivanov.decompose.value.Value

interface CreateSubscriptionComponent {
    data class Model(
        val subscriptionName: String,
        val subscriptionType: SubType
    )

    data class SubType(
        val smth: Boolean
    )

    val model: Value<Model>

    fun createSub()

    fun onNavigateBack()

    fun fillSubName(name: String)
}