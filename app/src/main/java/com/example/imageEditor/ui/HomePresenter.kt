package com.example.imageEditor.ui

import com.example.imageEditor.apiService.NetworkService
import com.example.imageEditor.base.BasePresenter
import com.example.imageEditor.model.CollectionModel

class HomePresenter(private val networkService: NetworkService) : HomeContract.Presenter {
    private var view: HomeContract.View? = null
    override fun getCollections(page: Int) {
        view?.setupCollections(networkService.getCollections(0))
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }

}