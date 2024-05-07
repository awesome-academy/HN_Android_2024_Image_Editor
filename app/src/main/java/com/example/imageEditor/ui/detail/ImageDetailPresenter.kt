package com.example.imageEditor.ui.detail

import android.graphics.Bitmap
import com.example.imageEditor.repository.DetailRepository

class ImageDetailPresenter(private val mDetailRepository: DetailRepository) :
    DetailContract.Presenter {
    private var mView: DetailContract.View? = null

    override fun downloadImage(url: String) {
        mDetailRepository.downloadImage(url)
    }

    override fun saveImage(bitmap: Bitmap) {
        mDetailRepository.saveImage(
            bitmap,
            onSuccess = { mView?.onSaveSuccess() },
            onError = { mView?.onSaveError(it) },
            onDownloading = { mView?.onDownloading() },
        )
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: DetailContract.View?) {
        this.mView = view
    }
}
