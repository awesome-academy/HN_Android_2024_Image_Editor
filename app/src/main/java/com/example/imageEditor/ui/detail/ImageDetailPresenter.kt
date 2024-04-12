package com.example.imageEditor.ui.detail

import com.example.imageEditor.repository.DetailRepository

class ImageDetailPresenter(private val detailRepository: DetailRepository) :
    DetailContract.Presenter {
    private var view: DetailContract.View? = null

    override fun downloadImage(url: String) {
        detailRepository.downloadImage(url)
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: DetailContract.View?) {
        this.view = view
    }
}
