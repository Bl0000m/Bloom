package kz.bloom.ui.auth.component

import com.arkivanov.decompose.value.Value
import kz.bloom.ui.main.component.MainComponent.Model

interface AuthComponent {

    val model: Value<Model>

    data class Model(
        val name: String
    )

}