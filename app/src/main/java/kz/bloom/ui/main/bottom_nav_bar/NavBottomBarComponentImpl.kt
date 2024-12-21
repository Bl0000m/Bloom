package kz.bloom.ui.main.bottom_nav_bar

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.ui.main.bottom_nav_bar.NavBottomBarComponent.Model
import kz.bloom.R
import kz.bloom.libraries.states
import kz.bloom.ui.main.api.MainApiClient
import kz.bloom.ui.main.store.MainStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext


enum class TabItem(val icon: Int, val label: String) {
    HOME(R.drawable.ic_home, "Home"),
    SEARCH(R.drawable.ic_search, "Search"),
    MENU(R.drawable.ic_menu, "Menu"),
    MARKET(R.drawable.ic_bag, "Market"),
    PROFILE(R.drawable.ic_user, "Profile")
}

class NavBottomBarComponentImpl(
    private val componentContext: ComponentContext,
    private val onTabSelect: (TabItem) -> Unit
) : NavBottomBarComponent, KoinComponent, ComponentContext by componentContext {
    private val _selectedTab = MutableValue(initialValue = TabItem.HOME)
    override val selectedTab: Value<TabItem> = _selectedTab

    private val mainApi by inject<MainApiClient>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()
    private val sharedPreferences by inject<SharedPreferencesSetting>()

    private val store: MainStore = instanceKeeper.getStore {
        MainStore(
            mainApi = mainApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory,
            sharedPreferences = sharedPreferences
        )
    }

    override val model: Value<Model> = store.states.toModels()

    override fun onTabSelected(tab: TabItem) {
        if (_selectedTab.value != tab) {
            _selectedTab.value = tab
            onTabSelect(tab)
            store.accept(MainStore.Intent.SelectNavBarItem(tab))
        }
    }

    override fun onOpenRestoredTab(tab: TabItem) {
        onTabSelect(tab)
    }
}

private fun Value<MainStore.State>.toModels(): Value<Model> = map { state ->
    Model(
        selectedTab = state.navBarSelectedItem
    )
}

