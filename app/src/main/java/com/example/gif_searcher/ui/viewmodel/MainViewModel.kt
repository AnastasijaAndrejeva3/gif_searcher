package com.example.gif_searcher.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gif_searcher.data.model.GifItem
import com.example.gif_searcher.network.GifRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GifRepository) : ViewModel() {
    private val _gifList = MutableLiveData<List<GifItem>>()
    val gifList: LiveData<List<GifItem>> get() = _gifList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var currentQuery = ""

    fun onSearchQueryChanged(query: String) {
        if (query.isNotEmpty()) {
            currentQuery = query
            GifRepository.GifsDataHolder.gifs.clear()
            GifRepository.GifsDataHolder.gifTitles.clear()
            searchGifs(query, loadMore = false)
        }
    }

    fun loadMoreGifs() {
        searchGifs(currentQuery, loadMore = true)
    }

    private fun searchGifs(query: String, loadMore: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.searchGifs(query, loadMore)
            _loading.value = false

            result.onSuccess { gifs ->
                if (loadMore) {
                    _gifList.value = _gifList.value.orEmpty() + gifs
                } else {
                    _gifList.value = gifs
                }
            }.onFailure { exception ->
                _errorMessage.value = "Oops, something went wrong: ${exception.message}"
            }
        }
    }
}
