package com.example.imageEditor.apiService

import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.model.PhotoSearchModel

class NetworkService(private val api: Api) {
    fun getCollections(page: Int): List<CollectionModel> {
        return api.getCollections(page)
    }

    fun searchPhotos(
        page: Int,
        query: String,
        onResult: (PhotoSearchModel?) -> Unit,
    ) {
        api.searchPhotos(page, query, onResult = onResult)
    }

    companion object {
        private var instance: NetworkService? = null

        fun getInstance(api: Api): NetworkService {
            if (instance == null) {
                instance = NetworkService(api)
            }
            return instance as NetworkService
        }
    }
}
