package com.example.imageEditor.ui.splash

import com.example.imageEditor.base.BasePresenter

interface AuthorizeContract {
    interface View {
        fun authorized()
    }

    interface Presenter : BasePresenter<View> {
        fun authorize(authorizationCode: String)
    }
}
