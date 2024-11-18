package kz.bloom.ui.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType
import kz.bloom.ui.auth.api.entity.CreateNewPassBody
import kz.bloom.ui.auth.api.entity.GetValidationCode
import kz.bloom.ui.auth.api.entity.RefreshAccessTokenRequestBody
import kz.bloom.ui.auth.api.entity.SendValidationCode
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
        }
    }

    override suspend fun enterAccount(username: String, password: String): SignInTokenResponse {
        val requestBody = SignInRequest(
            username = username,
            password = password
        )
        return client.post(AUTH_ENTER_ACCOUNT) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }.body<SignInTokenResponse>()
    }

    override suspend fun getConfirmCode(email: String): HttpResponse {
        val requestBody = GetValidationCode(
            email = email
        )
        return client.post(AUTH_CONFIRM_EMAIL_GET_CODE) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }
    }

    override suspend fun sendConfirmCode(email: String, code: String): HttpResponse {
        return client.post(AUTH_SEND_CONFIRM_CODE) {
            val requestBody = SendValidationCode(
                email = email,
                resetCode = code
            )
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }
    }

    override suspend fun createNewPass(
        email: String,
        password: String,
        confirmPassword: String
    ): HttpResponse {
        return client.put(AUTH_CREATE_NEW_PASS) {
            val requestBody = CreateNewPassBody(
                email = email,
                newPassword = password,
                confirmNewPassword = confirmPassword
            )
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): SignInTokenResponse {
        val requestBody = RefreshAccessTokenRequestBody(
            refreshToken = refreshToken
        )
        return client.post(AUTH_REFRESH_TOKEN) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }.body<SignInTokenResponse>()
    }

    companion object {
        const val AUTH_CREATE_ACCOUNT = "/v1/client/users"
        const val AUTH_ENTER_ACCOUNT = "/v1/users/login"
        const val AUTH_CONFIRM_EMAIL_GET_CODE = "/v1/client/users/reset-code"
        const val AUTH_SEND_CONFIRM_CODE = "/v1/client/users/reset-code/validate"
        const val AUTH_CREATE_NEW_PASS = "/v1/client/users/forgot-password"
        const val AUTH_REFRESH_TOKEN = "/v1/users/refresh"
    }
}