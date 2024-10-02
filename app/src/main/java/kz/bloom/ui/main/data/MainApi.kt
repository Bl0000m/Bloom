package kz.bloom.ui.main.data

import kz.bloom.ui.main.data.entity.ImageItem
import retrofit2.http.GET


interface MainApi {
    @GET("images")
    suspend fun getImages(): List<ImageItem>
}