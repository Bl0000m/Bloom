package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.main.data.entity.PageItem

interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed class Intent {
        object InitAuthorization : Intent()
    }

    data class State(
        val pagesList: List<PageItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : JvmSerializable
}