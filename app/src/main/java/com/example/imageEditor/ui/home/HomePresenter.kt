package com.example.imageEditor.ui.home

import com.example.imageEditor.repository.HomeRepository

class HomePresenter(private val homeRepository: HomeRepository) : HomeContract.Presenter {
    private var view: HomeContract.View? = null

    override fun getCollections(page: Int) {
        view?.setupCollections(homeRepository.getCollections(0))
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }
}
