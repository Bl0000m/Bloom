package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.main.bottom_nav_bar.NavBarItem
import kz.bloom.ui.main.bottom_nav_bar.TabItem
import kz.bloom.ui.main.data.entity.PageItem

interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed class Intent {
        data class SelectNavBarItem(val navBarItem: TabItem) : Intent()
    }

    data class State(
        val pagesList: List<PageItem> = emptyList(),
        val navBarSelectedItem: TabItem = TabItem.HOME,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : JvmSerializable
}