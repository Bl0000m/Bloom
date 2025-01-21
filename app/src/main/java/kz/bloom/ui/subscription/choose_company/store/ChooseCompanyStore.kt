package kz.bloom.ui.subscription.choose_company.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.CreateOrderRequestBody
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.State
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.Intent

interface ChooseCompanyStore : Store<Intent, State, Nothing> {
    data class State(
        val companiesRaw: BouquetDetailsResponse,
        val isError: Boolean,
        val orderFilled: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class FillOrder(val orderRequestBody: CreateOrderRequestBody) : Intent
    }
}