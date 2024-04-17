package com.example.imageEditor.apiService

import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.model.PhotoSearchModel

interface Api {
    fun getCollections(
        page: Int,
        onResult: (List<CollectionModel>?) -> Unit,
    )

    fun searchPhotos(
        page: Int,
        query: String,
        perPage: Int? = 20,
        onResult: (PhotoSearchModel?) -> Unit,
    )
}
