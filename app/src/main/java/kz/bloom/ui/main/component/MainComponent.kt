package kz.bloom.ui.main.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kz.bloom.ui.main.home_page.component.HomePageComponent
import kz.bloom.ui.main.profile.component.ProfileMainComponent

interface MainComponent {
    sealed interface Child {
        public data class Home(
            public val component: HomePageComponent
        ) : Child
//        public data class Search(
//            public val component: SearchComponent
//        )
//        public data class Menu(
//            public val component: MenuComponent
//        )
//        public data class Market(
//            public val component: MarketComponent
//        )
        public data class Profile(
            public val component: ProfileMainComponent
        ) : Child
    }

    public sealed interface Event {
        public data object OpenAuth: Event
    }

    public val events: Flow<Event>

    public val childStack: Value<ChildStack<*, Child>>

}