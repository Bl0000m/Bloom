package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.main.store.MainStore.Intent
import kz.bloom.ui.main.store.MainStore.State
import kz.bloom.ui.main.bottom_nav_bar.TabItem
import kz.bloom.ui.main.api.entity.PageItem

interface MainStore : Store<Intent, State, Nothing> {
    sealed interface Intent : JvmSerializable {
        data class SelectNavBarItem(val navBarItem: TabItem) : Intent
        data class GetUserInfo(val accessToken: String) : Intent
    }

    data class State(
        val pagesList: List<PageItem> = emptyList(),
        val navBarSelectedItem: TabItem = TabItem.HOME,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : JvmSerializable
}