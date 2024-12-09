package kz.bloom.ui.main.api

import kz.bloom.ui.main.api.entity.PageItem
import kz.bloom.ui.main.api.entity.UserInfoResponse


interface MainApi {
    suspend fun getImages(): List<PageItem>
    suspend fun getUserInfo(bearerToken: String) : UserInfoResponse
}