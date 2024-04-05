package com.example.imageEditor.ui

import com.example.imageEditor.base.BasePresenter
import com.example.imageEditor.base.BaseView
import com.example.imageEditor.model.CollectionModel

interface HomeContract {
    interface View: BaseView {
        fun setupCollections(list: List<CollectionModel>)
    }

    interface Presenter: BasePresenter<View> {
        fun getCollections(page: Int)
    }
}