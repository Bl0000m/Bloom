package kz.bloom.ui.profile.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.main.profile.component.ProfileMainComponent

interface ProfileRootComponent {
    public data class Model(
        val smth: Boolean
    )

    public sealed interface Child {
        public data class ProfileMain(
            public val component: ProfileMainComponent
        ) : Child
    }
    public val model: Value<Model>

    public val childStack: Value<ChildStack<*, Child>>
}