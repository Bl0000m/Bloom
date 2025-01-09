package kz.bloom.ui.subscription.choose_company.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.State
import kz.bloom.ui.subscription.choose_company.store.ChooseCompanyStore.Intent

interface ChooseCompanyStore : Store<Intent, State, Nothing> {
    data class State(
        val companiesRaw: BouquetDetailsResponse,
        val isError: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {

    }
}