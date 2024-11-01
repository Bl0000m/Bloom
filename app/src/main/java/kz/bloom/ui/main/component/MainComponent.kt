package kz.bloom.ui.main.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.main.content.NavBarItem
import kz.bloom.ui.main.data.entity.PageItem

interface MainComponent {
    public data class Model(
        val pages: List<PageItem>,
        val navBarSelectedItem: NavBarItem
    )

    public val model: Value<Model>

    public fun profileClicked()

    public fun categorySelected(navBarItem: NavBarItem)
}