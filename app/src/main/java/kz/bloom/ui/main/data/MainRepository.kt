package kz.bloom.ui.main.data

import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.data.entity.ImageItem
import kz.bloom.ui.main.data.entity.mockData

class MainRepository {
    suspend fun getImages(): List<ImageItem>{
        return mockData
    }

    fun getMockedState(): ImageListState {
        return ImageListState(images = mockData)
    }
}