package kz.bloom.ui.subscription.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponentImpl
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.component.SubscriptionRootComponent.Child
import kz.bloom.ui.subscription.component.SubscriptionRootComponent.Model
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponentImpl
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent.DateItem
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponentImpl
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponentImpl
import kz.bloom.ui.subscription.order_list.component.OrderListComponent
import kz.bloom.ui.subscription.order_list.component.OrderListComponentImpl
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent
import kz.bloom.ui.subscription.subs_list.component.SubsListComponentImpl

class SubscriptionRootComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
) : SubscriptionRootComponent,
    KoinComponent,
    ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    private val _childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SubsList,
        handleBackButton = true,
        childFactory = { configuration, componentContext ->
            createChild(
                configuration = configuration,
                componentContext = componentContext
            )
        }
    )
    private val _model = MutableValue(
        initialValue = Model(
            subscriptionName = "",
            subscriptionId = 0
        )
    )

    override val model: Value<Model> = _model
    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child = when (configuration) {
        is Configuration.SubsList -> Child.SubscriptionList(
            component = subsListComponent(
                componentContext = componentContext
            )
        )
        is Configuration.DatePicker -> Child.DatePicker(
            component = datePickerComponent(
                componentContext = componentContext
            )
        )
        is Configuration.CreateSubscription -> Child.CreateSubscription(
            component = createSubscriptionComponent(
                componentContext = componentContext
            )
        )
        is Configuration.OrderList -> Child.OrderList(
            component = orderListComponent(
                componentContext = componentContext
            )
        )
        is Configuration.FillDetails -> Child.FillDetails(
            component = fillDetailsComponent(
                componentContext = componentContext
            )
        )
        is Configuration.ChooseFlower -> Child.ChooseFlower(
            component = chooseFlower(
                componentContext = componentContext
            )
        )
    }

    private fun fillDetailsComponent(
        componentContext: ComponentContext
    ) : FillDetailsComponent = FillDetailsComponentImpl(
        componentContext = componentContext,
        onClosePressed = { },
        onChooseFlower = { navigation.pushNew(configuration = Configuration.ChooseFlower)}
    )

    private fun orderListComponent(
        componentContext: ComponentContext
    ) : OrderListComponent = OrderListComponentImpl(
        componentContext = componentContext,
        onNavigateRightBack = { navigation.popToFirst() },
        subscriptionId = _model.value.subscriptionId,
        onFillOrder = { navigation.pushNew(configuration = Configuration.FillDetails) }
    )

    private fun createSubscriptionComponent(
        componentContext: ComponentContext
    ) : CreateSubscriptionComponent  = CreateSubscriptionComponentImpl(
        componentContext = componentContext,
        onBackPressed = { navigation.pop() },
        onCreate = { name ->
            _model.update { it.copy(subscriptionName = name) }
            navigation.pushNew(configuration = Configuration.DatePicker)
        }
    )

    private fun subsListComponent(
        componentContext: ComponentContext
    ) : SubsListComponent = SubsListComponentImpl(
        componentContext = componentContext,
        onCreateSubscription = { navigation.pushNew(configuration = Configuration.CreateSubscription) },
        onBackPress = { onNavigateBack() }
    )

    private fun datePickerComponent(
        componentContext: ComponentContext
    ) : DatePickerComponent = DatePickerComponentImpl(
        componentContext = componentContext,
        onContinuePressed = { subscriptionId ->
            _model.update { it.copy(subscriptionId = subscriptionId) }
            navigation.pushNew(configuration = Configuration.OrderList(subscriptionName = _model.value.subscriptionName))
        },
        onBackPress = {
            navigation.pop()
        },
        subscriptionName = _model.value.subscriptionName,
        subscriptionTypeId = "1"
    )

    private fun chooseFlower(
        componentContext: ComponentContext
    ) : ChooseFlowerComponent = ChooseFlowerComponentImpl(
        componentContext = componentContext
    )

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object SubsList : Configuration
        @Serializable
        data object DatePicker : Configuration
        @Serializable
        data object CreateSubscription : Configuration
        @Serializable
        data class OrderList(val subscriptionName: String) : Configuration
        @Serializable
        data object FillDetails : Configuration
        @Serializable
        data object ChooseFlower : Configuration
    }
}