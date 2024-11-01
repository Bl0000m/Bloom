package kz.bloom.ui.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kz.bloom.ui.auth.api.entity.AuthRequest
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

internal class AuthApiClient(private val client: HttpClient) : AuthApi {

    override suspend fun createAccount(model: SignUpComponent.Model): HttpResponse {
        val requestBody = AuthRequest(
            name = model.name,
            email = model.email,
            phoneNumber = model.phoneNumber,
            password = model.password,
            confirmPassword = model.passwordConfirm
        )
        return client.post(AUTH_CREATE_ACCOUNT) {
            setBody(requestBody)
        }.body()
    }

    companion object {
        const val AUTH_CREATE_ACCOUNT = "/v1/client/users"
    }
}