package kz.bloom.ui.auth.api

import com.arkivanov.decompose.value.Value
import io.ktor.client.statement.HttpResponse
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

interface AuthApi {
    suspend fun createAccount(model: SignUpComponent.Model) : HttpResponse
}