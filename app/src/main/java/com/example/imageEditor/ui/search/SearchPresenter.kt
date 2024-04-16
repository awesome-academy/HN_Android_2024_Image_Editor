package com.example.imageEditor.ui.search

import com.example.imageEditor.repository.SearchRepository
import com.example.imageEditor.utils.ALL

class SearchPresenter(private val searchRepository: SearchRepository) : SearchContract.Presenter {
    private var mQuery = ALL
    private var mView: SearchContract.View? = null

    override fun searchPhotos(
        page: Int,
        query: String?,
    ) {
        query?.let { mQuery = it }
        searchRepository.searchPhotos(page, mQuery) {
            mView?.setupData(it)
        }
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: SearchContract.View?) {
        this.mView = view
    }
}
