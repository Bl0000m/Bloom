package kz.bloom.ui.auth.pass_code.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.State
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Label
import kz.bloom.ui.auth.pass_code.store.PassCodeStore.Intent


interface PassCodeStore : Store<Intent, State, Label> {
    data class State(
        val isError: Boolean,
        val isLoading: Boolean,
        val tokenRefreshed: Boolean,
        val serverIsNotResponding: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class RefreshAccessToken(val refreshToken: String) : Intent
    }

    sealed interface Label : JvmSerializable {
        data class ErrorReceived(val message: String) : Label
    }
}


