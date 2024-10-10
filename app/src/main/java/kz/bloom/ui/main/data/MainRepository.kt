package kz.bloom.ui.main.data

import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.data.entity.PageItem
import kz.bloom.ui.main.data.entity.mockData

class MainRepository {
    suspend fun getImages(): List<PageItem>{
        return mockData
    }

    fun getMockedState(): ImageListState {
        return ImageListState(pages = mockData)
    }
}