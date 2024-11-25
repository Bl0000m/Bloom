package kz.bloom.ui.main.home_page.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kz.bloom.libraries.states
import kz.bloom.ui.main.data.MainRepository
import org.koin.core.component.KoinComponent
import kz.bloom.ui.main.home_page.component.HomePageComponent.Model
import kz.bloom.ui.main.store.MainStore
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class HomePageComponentImpl(
    componentContext: ComponentContext
) : HomePageComponent, KoinComponent, ComponentContext by componentContext {

    private val mainApi by inject<MainRepository>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val ioContext by inject<CoroutineContext>(qualifier = named(name = "IO"))
    private val storeFactory by inject<StoreFactory>()

    private val store: MainStore = instanceKeeper.getStore {
        MainStore(
            mainApi = mainApi,
            mainContext = mainContext,
            ioContext = ioContext,
            storeFactory = storeFactory)
    }

    override val model: Value<Model> = store.states.toModels()

    private fun Value<MainStore.State>.toModels(): Value<Model> = map { state ->
        Model(
            pages = state.pagesList
        )
    }
}