package kz.bloom.ui.main.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.contentType
import kz.bloom.ui.main.api.entity.PageItem
import kz.bloom.ui.main.api.entity.UserInfoResponse
import kz.bloom.ui.main.api.entity.mockData


internal class MainApiClient(private val client: HttpClient) : MainApi {

    override suspend fun getImages(): List<PageItem>{
        return mockData
    }

    override suspend fun getUserInfo(bearerToken: String): UserInfoResponse {
        return client.get(GET_USER_INFO) {
            contentType(io.ktor.http.ContentType.Application.Json)
            headers.append("Authorization", "Bearer $bearerToken")
        }.body<UserInfoResponse>()
    }

    companion object {
        const val GET_USER_INFO = "v1/users/me"
    }
}