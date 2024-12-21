package kz.bloom.ui.subscription.choose_flower.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.State
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto

interface ChooseFlowerStore : Store<Nothing, State, Nothing> {
    data class State(
        val bouquets: List<BouquetDTO>,
        val isError: Boolean,
        val isLoading: Boolean
    ) : JvmSerializable


    @Serializable
    data class BouquetDTO(
        val id: Long,
        val name: String,
        val description: String,
        val bouquetPhotos: List<BouquetPhoto>,
        val price: Double,
        val addition: String
    )
}