package com.example.imageEditor.ui.search.adapter

interface DeleteItemCallback {
    fun deleteItemQuery(
        id: Long,
        position: Int,
    )
}
