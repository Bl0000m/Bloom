package kz.bloom.ui.main.home_page.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.main.api.entity.PageItem

interface HomePageComponent {
    public data class Model(
        val pages: List<PageItem>
    )

    public val model: Value<Model>
}