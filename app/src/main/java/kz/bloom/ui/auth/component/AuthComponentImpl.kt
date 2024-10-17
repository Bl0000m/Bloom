package kz.bloom.ui.auth.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.component.AuthComponent.Model

class AuthComponentImpl(
    componentContext: ComponentContext
) : AuthComponent,
    ComponentContext by componentContext
{
    private val _model: MutableValue<Model> = MutableValue(
        initialValue = Model(
            name = ""
        )
    )

    override val model: Value<Model> = _model

}