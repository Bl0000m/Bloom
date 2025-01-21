package kz.bloom.ui.subscription.address_list.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.subscription.address_list.store.AddressListStore.State
import kz.bloom.ui.subscription.address_list.store.AddressListStore.Intent

interface AddressListStore : Store<Intent, State, Nothing> {

    data class State(
        val isError: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {

    }

}