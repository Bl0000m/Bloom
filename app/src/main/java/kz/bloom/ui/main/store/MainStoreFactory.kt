package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.bloom.ui.main.bottom_nav_bar.NavBarItem
import kz.bloom.ui.main.bottom_nav_bar.TabItem
import kz.bloom.ui.main.data.MainRepository
import kz.bloom.ui.main.data.entity.PageItem
import kz.bloom.ui.main.store.MainStore.Intent
import kz.bloom.ui.main.store.MainStore.State
import kotlin.coroutines.CoroutineContext

private sealed interface Action : JvmSerializable {
    object LoadPagesList: Action
}

private sealed interface Message : JvmSerializable {

    data class PagesLoaded(val pages : List<PageItem>) : Message

    data class LoadingPagesList(val isLoading: Boolean) : Message

    data class NavBarItemSelected(val selectedItem: TabItem) : Message

    object ErrorOccurred : Message
}

internal fun MainStore(
    mainApi: MainRepository,
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
    storeFactory: StoreFactory
) : MainStore =
    object : MainStore, Store<Intent, State, Nothing>
            by storeFactory.create<Intent, Action, Message, State, Nothing> (
                name = "MainStore",
                initialState = State(
                    pagesList = emptyList(),
                    isLoading = false,
                    error = null
                ),
                reducer = { message ->
                    when(message) {
                        is Message.ErrorOccurred -> copy(
                            isLoading = false,
                            error = ""
                        )
                        is Message.LoadingPagesList -> copy(
                            isLoading = message.isLoading
                        )
                        is Message.PagesLoaded -> copy(
                            pagesList = message.pages
                        )
                        is Message.NavBarItemSelected -> copy(
                            navBarSelectedItem = message.selectedItem
                        )
                    }
                },
                bootstrapper = SimpleBootstrapper(Action.LoadPagesList),
                executorFactory = {
                    ExecutorImpl(
                        mainApi = mainApi,
                        mainContext = mainContext,
                        ioContext = ioContext
                    )
                }
            ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val mainApi: MainRepository
) : CoroutineExecutor<Intent, Action, State, Message, Nothing> (mainContext = mainContext)
{
    override fun executeAction(action: Action, getState: () -> State) {
        super.executeAction(action, getState)
        when(action) {
            is Action.LoadPagesList -> {
                scope.launch {
                    try {
                        dispatch(message = Message.LoadingPagesList(isLoading = true))

                        val pagesResponse = withContext(ioContext) {
                            async { mainApi.getImages() }
                        }

                        val pages = pagesResponse.await()

                        dispatch(message = Message.PagesLoaded(pages = pages))

                    } catch (exception : Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
        when (intent) {
            is Intent.SelectNavBarItem -> {
                scope.launch {
                    try {
                        dispatch(message = Message.NavBarItemSelected(selectedItem = intent.navBarItem))
                    } catch (exception : Exception) {
                        dispatch(message = Message.ErrorOccurred)
                    }
                }
            }
        }
    }
}