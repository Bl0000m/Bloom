package kz.bloom.ui.auth.api

import io.ktor.client.statement.HttpResponse
import kz.bloom.ui.auth.api.entity.SignInTokenResponse
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

interface AuthApi {
    suspend fun createAccount(model: SignUpComponent.Model) : HttpResponse
    suspend fun enterAccount(model: SignInComponent.Model) : SignInTokenResponse
    suspend fun getConfirmCode(email: String) : HttpResponse
    suspend fun sendConfirmCode(email: String, code: String) : HttpResponse
    suspend fun createNewPass(email: String, password: String, confirmPassword: String) : HttpResponse
    suspend fun refreshAccessToken(refreshToken: String) : SignInTokenResponse
}