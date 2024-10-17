package kz.bloom.ui.main.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kz.bloom.ui.main.data.MainApi
import kz.bloom.ui.main.store.MainStore.Intent
import kz.bloom.ui.main.store.MainStore.State
import kotlin.coroutines.CoroutineContext

private sealed interface Action : JvmSerializable {
    object LoadPagesList: Action
}

private sealed interface Message : JvmSerializable {
    object ErrorOccurred : Message
}

internal fun MainStore(
    mainApi: MainApi,
    mainContext: CoroutineContext,
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
                    }
                },
                bootstrapper = SimpleBootstrapper(),
                executorFactory = {
                    ExecutorImpl(
                        mainApi = mainApi,
                        mainContext = mainContext
                    )
                }
            ) {}

private class ExecutorImpl(
    mainContext: CoroutineContext,
    private val mainApi: MainApi
) : CoroutineExecutor<Intent, Action, State, Message, Nothing> (mainContext = mainContext)
{
    override fun executeAction(action: Action, getState: () -> State) {
        super.executeAction(action, getState)
        when(action) {
            is Action.LoadPagesList -> {

            }
        }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        super.executeIntent(intent, getState)
    }
}