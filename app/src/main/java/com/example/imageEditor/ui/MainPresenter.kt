package com.example.imageEditor.ui

import com.example.imageEditor.repository.HomeRepository

class MainPresenter(private val homeRepository: HomeRepository) : MainContract.Presenter {
    private var view: MainContract.View? = null

    override fun getCollections(page: Int) {
        view?.setupCollections(homeRepository.getCollections(0))
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: MainContract.View?) {
        this.view = view
    }
}
