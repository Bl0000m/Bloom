package kz.bloom.ui.main.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.bloom.ui.main.data.MainRepository
import kz.bloom.ui.main.data.entity.ImageListState

class ImageListViewModel(private val repository: MainRepository) : ViewModel() {
    private val _state = MutableLiveData<ImageListState>()
    val state: LiveData<ImageListState> = _state

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _state.value = repository.getMockedState()
    }
}