package kz.bloom.ui.subscription.flower_details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.flower_details.store.FlowerDetailsStore.State
import kz.bloom.ui.subscription.flower_details.store.FlowerDetailsStore.Intent

interface FlowerDetailsStore : Store<Intent, State, Nothing> {
    data class State(
        val bouquetDetailsResponse: BouquetDetailsResponse,
        val isError: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {

    }
}