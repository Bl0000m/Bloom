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
import java.time.LocalDate
import kotlinx.serialization.Serializable
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
            subscriptionName = ""
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
        is Configuration.FillDetails -> Child.FillDetails(
            component = fillDetailsComponent(
                componentContext = componentContext,
                configuration.selection
            )
        )
    }

    private fun fillDetailsComponent(
        componentContext: ComponentContext,
        selection:List<DateItem>
    ) : FillDetailsComponent = FillDetailsComponentImpl(
        componentContext = componentContext,
        selection = selection,
        onNavigateRightBack = { navigation.popToFirst() }
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
        onContinuePressed = { selection ->
            navigation.pushNew(configuration = Configuration.FillDetails(subscriptionName = _model.value.subscriptionName, selection = selection))
        },
        onBackPress = {
            navigation.pop()
        }
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
        data class FillDetails(val subscriptionName: String, val selection: List<DateItem>) : Configuration
    }
}