package com.example.imageEditor.ui.favourite

import com.example.imageEditor.repository.FavoriteRepository

class FavouritePresenter(private val mFavoriteRepository: FavoriteRepository) :
    FavouriteContract.Presenter {
    private var mView: FavouriteContract.View? = null

    override fun getFavoriteList(
        name: String,
        page: Int,
    ) {
        mFavoriteRepository.getFavoriteList(
            name,
            page,
            onResult = { mView?.setFavoriteList(it) },
            onFailure = { mView?.onFailure() },
        )
    }

    override fun likeImage(id: String) {
        mFavoriteRepository.likeImage(id) {
            mView?.onFailure()
        }
    }

    override fun dislikeImage(id: String) {
        mFavoriteRepository.dislikeImage(id) {
            mView?.onFailure()
        }
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: FavouriteContract.View?) {
        mView = view
    }
}
