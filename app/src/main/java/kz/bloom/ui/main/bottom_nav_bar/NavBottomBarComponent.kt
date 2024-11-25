package kz.bloom.ui.main.bottom_nav_bar

import com.arkivanov.decompose.value.Value

interface NavBottomBarComponent {
    data class Model(
        val selectedTab: TabItem
    )

    val model: Value<Model>

    val selectedTab: Value<TabItem>

    fun onTabSelected(tab: TabItem)
}

