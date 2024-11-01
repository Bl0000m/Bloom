package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.main.content.NavBarItem
import kz.bloom.ui.main.data.entity.PageItem

interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed class Intent {
        data class SelectNavBarItem(val navBarItem: NavBarItem) : Intent()
    }

    data class State(
        val pagesList: List<PageItem> = emptyList(),
        val navBarSelectedItem: NavBarItem = NavBarItem.HOME,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : JvmSerializable
}