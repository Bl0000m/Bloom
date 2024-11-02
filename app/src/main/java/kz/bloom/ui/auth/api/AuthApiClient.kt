package kz.bloom.ui.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType
import kz.bloom.ui.auth.api.entity.SignInRequest
import kz.bloom.ui.auth.api.entity.SignInTokenResponse
import kz.bloom.ui.auth.api.entity.SignUpRequest
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

internal class AuthApiClient(private val client: HttpClient) : AuthApi {

    override suspend fun createAccount(model: SignUpComponent.Model): HttpResponse {

        val requestBody = SignUpRequest(
            name = model.name,
            email = model.email,
            phoneNumber = model.phoneNumber,
            password = model.password,
            confirmPassword = model.passwordConfirm
        )
        return client.post(AUTH_CREATE_ACCOUNT) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }

    override suspend fun enterAccount(model: SignInComponent.Model): SignInTokenResponse {
        val requestBody = SignInRequest(
            username = model.email,
            password = model.password
        )
        return client.post(AUTH_ENTER_ACCOUNT) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }.body<SignInTokenResponse>()
    }

    companion object {
        const val AUTH_CREATE_ACCOUNT = "/v1/client/users"
        const val AUTH_ENTER_ACCOUNT = "/v1/users/login"
    }
}