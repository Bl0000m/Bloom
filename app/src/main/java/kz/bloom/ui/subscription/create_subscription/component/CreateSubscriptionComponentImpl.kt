package kz.bloom.ui.subscription.create_subscription.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent.Model
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent.Event
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent.SubType

class CreateSubscriptionComponentImpl(
    componentContext: ComponentContext,
    private val onBackPressed: () -> Unit,
    private val onCreate: (subscriptionName: String) -> Unit
) : CreateSubscriptionComponent, KoinComponent, ComponentContext by componentContext {
    private val _model = MutableValue(
        initialValue =
        Model(
            subscriptionName = "",
            pickedSubscriptionName = "",
            subscriptionNameErrorText = "",
            subscriptionTypeError = "",
            subscriptionType = listOf(
                SubType(name = "Любые цветы каждые 2 дня", number = 1),
                SubType(name = "Избранная композиция за период", number = 2)
            )
        )
    )

    private val _uiEvents: MutableSharedFlow<Event> = MutableSharedFlow(replay = 0)

    override val model: Value<Model> = _model

    override val events: Flow<Event> = _uiEvents

    override fun createSub() {
        if (_model.value.pickedSubscriptionName.isNotEmpty() && _model.value.subscriptionName.isNotEmpty()) {
            onCreate(_model.value.subscriptionName)
        } else if (_model.value.subscriptionName.isEmpty()) {
            _model.update { it.copy(subscriptionNameErrorText = "Заполните поле.") }
        } else {
            _model.update { it.copy(subscriptionTypeError = "Выберите тип подписки.") }
        }
    }

    override fun onNavigateBack() {
        onBackPressed()
    }

    override fun fillSubName(name: String) {
        _model.update {
            it.copy(
                subscriptionName = name,
                subscriptionNameErrorText = ""
            )
        }
    }

    override fun chosenComposition(subName: String) {
        _model.update {
            it.copy(
                pickedSubscriptionName = subName,
                subscriptionTypeError = ""
            )
        }
    }
}