package com.example.imageEditor.apiService

import com.example.imageEditor.model.CollectionModel

interface Api {
    fun getCollections(page: Int): List<CollectionModel>
}
