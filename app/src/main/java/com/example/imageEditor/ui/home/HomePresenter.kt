package com.example.imageEditor.ui.home

import com.example.imageEditor.repository.HomeRepository

class HomePresenter(private val homeRepository: HomeRepository) : HomeContract.Presenter {
    private var view: HomeContract.View? = null

    override fun getCollections(page: Int) {
        homeRepository.getCollections(page) { data ->
            data?.let { view?.setupCollections(it) }
        }
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }
}
