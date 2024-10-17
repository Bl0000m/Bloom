package kz.bloom.ui.main.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.main.data.entity.PageItem

interface MainComponent {
    public data class Model(
        val pagesList: List<PageItem>
    )

    public val model: Value<Model>

    public fun navigateToAuthorization()
}