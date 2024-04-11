package com.example.imageEditor.ui.home

import com.example.imageEditor.base.BasePresenter
import com.example.imageEditor.model.CollectionModel

interface HomeContract {
    interface View {
        fun setupCollections(list: List<CollectionModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getCollections(page: Int)
    }
}
