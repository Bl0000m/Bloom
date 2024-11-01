package kz.bloom.ui.auth.sign_up.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

import kz.bloom.ui.auth.sign_up.store.AuthStore.State
import kz.bloom.ui.auth.sign_up.store.AuthStore.Intent

interface AuthStore : Store<Intent, State, Nothing> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val isSuccess: Boolean
    ) : JvmSerializable

    sealed interface Intent : JvmSerializable {
        data class CreateAccount(val model: SignUpComponent.Model) : Intent
    }
}