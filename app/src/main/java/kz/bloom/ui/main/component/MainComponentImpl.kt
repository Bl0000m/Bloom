package kz.bloom.ui.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory

import kz.bloom.ui.main.component.MainComponent.Model
import kz.bloom.ui.main.data.MainApi
import kz.bloom.ui.main.store.MainStore

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext


public class MainComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateAuth:() -> Unit
) :
MainComponent,
KoinComponent,
ComponentContext by componentContext
{
    private val mainApi by inject<MainApi>()
    private val mainContext by inject<CoroutineContext>(qualifier = named(name = "Main"))
    private val storeFactory by inject<StoreFactory>()

    private val store: MainStore = instanceKeeper.getStore {
        MainStore(
            mainApi = mainApi,
            mainContext = mainContext,
            storeFactory = storeFactory)
    }


    private val _model: MutableValue<Model> = MutableValue(
        initialValue = Model(
            pagesList = emptyList()
        )
    )

    override val model: Value<Model> = _model

    override fun navigateToAuthorization() {
        onNavigateAuth()
    }
}