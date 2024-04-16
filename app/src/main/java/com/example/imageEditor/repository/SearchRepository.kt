package com.example.imageEditor.repository

import com.example.imageEditor.apiService.ApiImpl
import com.example.imageEditor.apiService.NetworkService
import com.example.imageEditor.base.OnListenProcess
import com.example.imageEditor.model.PhotoSearchModel

class SearchRepository(onListenProcess: OnListenProcess) {
    private val networkService = NetworkService.getInstance(ApiImpl.getInstance(onListenProcess))

    fun searchPhotos(
        page: Int,
        query: String,
        onResult: (PhotoSearchModel?) -> Unit,
    ) {
        networkService.searchPhotos(page, query, onResult)
    }

    companion object {
        private var instance: SearchRepository? = null

        @JvmStatic
        fun getInstance(onListenProcess: OnListenProcess): SearchRepository {
            if (instance == null) {
                instance = SearchRepository(onListenProcess)
            }
            return instance as SearchRepository
        }
    }
}
