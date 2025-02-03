package kz.bloom.ui.subscription.component

import android.content.Context
import androidx.core.view.KeyEventDispatcher.Component
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.serialization.Serializable
import kz.bloom.ui.auth.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.auth.country_chooser.component.ChooseCountryComponentImpl
import kz.bloom.ui.auth.country_chooser.component.CountryModel
import kz.bloom.ui.subscription.add_address.component.AddAddressComponent
import kz.bloom.ui.subscription.add_address.component.AddAddressComponentImpl
import kz.bloom.ui.subscription.address_list.component.AddressListComponent
import kz.bloom.ui.subscription.address_list.component.AddressListComponentImpl
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponent
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponentImpl
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponentImpl
import org.koin.core.component.KoinComponent
import kz.bloom.ui.subscription.component.SubscriptionRootComponent.Child
import kz.bloom.ui.subscription.component.SubscriptionRootComponent.Model
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponentImpl
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponent
import kz.bloom.ui.subscription.date_picker.component.DatePickerComponentImpl
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponent
import kz.bloom.ui.subscription.fill_details.component.FillDetailsComponentImpl
import kz.bloom.ui.subscription.flower_details.component.FlowerDetailsComponent
import kz.bloom.ui.subscription.flower_details.component.FlowerDetailsComponentImpl
import kz.bloom.ui.subscription.order_list.component.OrderListComponent
import kz.bloom.ui.subscription.order_list.component.OrderListComponentImpl
import kz.bloom.ui.subscription.subs_list.component.SubsListComponent
import kz.bloom.ui.subscription.subs_list.component.SubsListComponentImpl

class SubscriptionRootComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val context: Context
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

    private var currentAddAddressComponent: AddAddressComponent? = null

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
                componentContext = componentContext,
                orderId = configuration.orderId,
                deliveryDate = configuration.deliveryDate
            )
        )

        is Configuration.ChooseFlower -> Child.ChooseFlower(
            component = chooseFlower(
                componentContext = componentContext,
                orderId = configuration.orderId
            )
        )

        is Configuration.FlowerDetails -> Child.FlowerDetails(
            component = flowerDetails(
                componentContext = componentContext,
                bouquetDTO = configuration.bouquetDTO,
                orderId = configuration.orderId
            )
        )

        is Configuration.ChooseCompany -> Child.ChooseCompany(
            component = chooseCompany(
                componentContext = componentContext,
                bouquetId = configuration.bouquetId,
                orderId = configuration.orderId
            )
        )

        is Configuration.AddressList -> Child.AddressList(
            component = addressList(
                componentContext = componentContext,
                orderId = configuration.orderId
            )
        )

        is Configuration.ChooseCountry -> Child.CountryChoose(
            component = chooseCountryComponent(
                componentContext = componentContext,
                onCountrySelected = { selectedCountry ->
                    currentAddAddressComponent?.updateSelectedCountry(selectedCountry)
                    navigation.pop()
                }
            )
        )

        is Configuration.AddAddress -> {
            if (currentAddAddressComponent == null) {
                currentAddAddressComponent = addAddress(
                    componentContext = componentContext,
                    orderId = configuration.orderId,
                    selectedCountry = configuration.selectedCountry
                )
            }
            Child.AddAddress(component = currentAddAddressComponent!!)
        }
    }

    private fun addAddress(
        componentContext: ComponentContext,
        selectedCountry: CountryModel?,
        orderId: Long
    ): AddAddressComponent = AddAddressComponentImpl(
        componentContext = componentContext,
        orderId = orderId,
        selectedCountry = selectedCountry,
        onNavigateBack = { navigation.pop() },
        onOpenCountryChooser = {
            navigation.pushNew(
                configuration = Configuration.ChooseCountry
            )
        },
        onNavigateToOrderDetails = {
            navigation.popWhile { configuration ->
                configuration !is Configuration.FillDetails
            }
        }
    )

    private fun chooseCountryComponent(
        componentContext: ComponentContext,
        onCountrySelected: (CountryModel) -> Unit,
    ): ChooseCountryComponent = ChooseCountryComponentImpl(
        componentContext = componentContext,
        onCountrySelected = { selectedCountry ->
            onCountrySelected(selectedCountry)
        },
        onNavigateBack = { navigation.pop() },
        context = context
    )

    private fun addressList(
        componentContext: ComponentContext,
        orderId: Long
    ): AddressListComponent = AddressListComponentImpl(
        componentContext = componentContext,
        onAddAddress = {
            navigation.pushNew(
                configuration = Configuration.AddAddress(
                    orderId = orderId,
                    selectedCountry = null)
            )
        },
        onNavigateBack = { navigation.pop() }
    )

    private fun chooseCompany(
        componentContext: ComponentContext,
        bouquetId: Long,
        orderId: Long
    ): ChooseCompanyComponent = ChooseCompanyComponentImpl(
        componentContext = componentContext,
        bouquetId = bouquetId,
        orderId = orderId,
        onBranchPicked = {
            navigation.popWhile { configuration ->
                configuration !is Configuration.FillDetails
            }
        },
        onNavigateBack = { navigation.pop() }
    )

    private fun flowerDetails(
        componentContext: ComponentContext,
        bouquetDTO: BouquetDTO,
        orderId: Long
    ): FlowerDetailsComponent = FlowerDetailsComponentImpl(
        componentContext = componentContext,
        bouquetDTO = bouquetDTO,
        onCloseDetails = { navigation.pop() },
        onBouquetPicked = { bouquetId ->
            navigation.pushNew(
                configuration = Configuration.ChooseCompany(
                    bouquetId = bouquetId,
                    orderId = orderId
                )
            )
        }
    )

    private fun orderListComponent(
        componentContext: ComponentContext
    ): OrderListComponent = OrderListComponentImpl(
        componentContext = componentContext,
        onNavigateRightBack = { navigation.popToFirst() },
        subscriptionId = _model.value.subscriptionId,
        onFillOrder = { orderId, deliveryDate ->
            navigation.pushNew(configuration = Configuration.FillDetails(orderId = orderId, deliveryDate = deliveryDate))
        }
    )

    private fun fillDetailsComponent(
        componentContext: ComponentContext,
        orderId: Long,
        deliveryDate: String
    ): FillDetailsComponent = FillDetailsComponentImpl(
        componentContext = componentContext,
        onClosePressed = { navigation.pop() },
        onChooseFlower = { navigation.pushNew(configuration = Configuration.ChooseFlower(orderId = orderId)) },
        onAddressClicked = { navigation.pushNew(configuration = Configuration.AddressList(orderId = orderId)) },
        orderId = orderId,
        deliveryDate = deliveryDate
    )

    private fun createSubscriptionComponent(
        componentContext: ComponentContext
    ): CreateSubscriptionComponent = CreateSubscriptionComponentImpl(
        componentContext = componentContext,
        onBackPressed = { navigation.pop() },
        onCreate = { name ->
            _model.update { it.copy(subscriptionName = name) }
            navigation.pushNew(configuration = Configuration.DatePicker)
        }
    )

    private fun subsListComponent(
        componentContext: ComponentContext
    ): SubsListComponent = SubsListComponentImpl(
        componentContext = componentContext,
        onCreateSubscription = { navigation.pushNew(configuration = Configuration.CreateSubscription) },
        onBackPress = { onNavigateBack() }
    )

    private fun datePickerComponent(
        componentContext: ComponentContext
    ): DatePickerComponent = DatePickerComponentImpl(
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
        componentContext: ComponentContext,
        orderId: Long
    ): ChooseFlowerComponent = ChooseFlowerComponentImpl(
        componentContext = componentContext,
        onCloseBouquet = { navigation.pop() },
        onFlowerConsidered = { bouquetDTO ->
            navigation.pushNew(
                configuration = Configuration.FlowerDetails(
                    bouquetDTO = bouquetDTO,
                    orderId = orderId
                )
            )
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
        data class OrderList(val subscriptionName: String) : Configuration

        @Serializable
        data class FillDetails(val orderId: Long, val deliveryDate: String) : Configuration

        @Serializable
        data class ChooseFlower(val orderId: Long) : Configuration

        @Serializable
        data class FlowerDetails(val bouquetDTO: BouquetDTO, val orderId: Long) : Configuration

        @Serializable
        data class ChooseCompany(val bouquetId: Long, val orderId: Long) : Configuration

        @Serializable
        data class AddressList(val orderId: Long) : Configuration

        @Serializable
        data class AddAddress(
            val orderId: Long,
            val selectedCountry: CountryModel?
        ) : Configuration

        @Serializable
        data object ChooseCountry : Configuration
    }
}