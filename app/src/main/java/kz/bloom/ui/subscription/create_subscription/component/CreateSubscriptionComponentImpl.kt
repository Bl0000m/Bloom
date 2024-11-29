package kz.bloom.ui.subscription.create_subscription.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent.Model
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent.SubType

class CreateSubscriptionComponentImpl(
    componentContext: ComponentContext,
    private val onBackPressed:() -> Unit,
    private val onCreate:() -> Unit
) : CreateSubscriptionComponent, KoinComponent, ComponentContext by componentContext {
    private val _model = MutableValue(initialValue =
    Model(
        subscriptionName = "",
        subscriptionType = SubType(false)
    )
    )

    override val model: Value<Model> = _model

    override fun createSub() {
        onCreate()
    }

    override fun onNavigateBack() {
        onBackPressed()
    }

    override fun fillSubName(name: String) {
        _model.update { it.copy(subscriptionName = name) }
    }
}