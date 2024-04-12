package com.example.imageEditor.ui.detail

import com.example.imageEditor.base.BasePresenter

interface DetailContract {
    interface View

    interface Presenter : BasePresenter<View> {
        fun downloadImage(url: String)
    }
}
