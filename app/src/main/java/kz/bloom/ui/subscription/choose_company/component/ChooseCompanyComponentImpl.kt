package kz.bloom.ui.subscription.choose_company.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.subscription.api.SubscriptionApi
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.CreateOrderRequestBody
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponent.Model
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponent.Company
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.State
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.Intent
import kz.bloom.ui.subscription.choose_company.store.chooseCompanyStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class ChooseCompanyComponentImpl(
    componentContext: ComponentContext,
    private val bouquetId: Long,
    private val orderId: Long,
    private val onBranchPicked: () -> Unit,
    private val onNavigateBack: () -> Unit
) : ChooseCompanyComponent, KoinComponent, ComponentContext by componentContext {

    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()
    private val subscriptionApi by inject<SubscriptionApi>()

    private val store = chooseCompanyStore(
        mainContext = mainContext,
        ioContext = ioContext,
        storeFactory = storeFactory,
        sharedPreferences = sharedPreferences,
        subscriptionApi = subscriptionApi,
        bouquetId = bouquetId
    )

    override val model: Value<Model> = store.states.toModels()

    private fun Value<State>.toModels(): Value<Model> = map { state ->
        Model(
            companies = state.companiesRaw.toCompanies()
        )
    }

    override fun branchPicked(branchId: Long, price: String) {
        store.accept(
            intent = Intent.FillOrder(
                CreateOrderRequestBody(
                    id = orderId,
                    bouquetId = bouquetId,
                    branchDivisionId = branchId,
                    assemblyCost = price.toDouble(),
                    address = "Дом калатушкино"
                )
            )
        )
        store.states.subscribe { state ->
            if(state.orderFilled) {
                onBranchPicked()
            }
        }
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    private fun BouquetDetailsResponse.toCompanies(): List<Company> {
        return branchBouquetInfo.map { branchBouquetInfo ->
            Company(
                companyName = branchBouquetInfo.divisionType,
                price = branchBouquetInfo.price.toString(),
                branchId = branchBouquetInfo.branchId
            )
        }
    }
}