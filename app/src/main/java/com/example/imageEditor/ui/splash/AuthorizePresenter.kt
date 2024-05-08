package com.example.imageEditor.ui.splash

import com.example.imageEditor.model.request.AuthorizeRequest
import com.example.imageEditor.repository.AuthorizeRepository
import com.example.imageEditor.utils.ACCESS_KEY
import com.example.imageEditor.utils.REDIRECT_URI
import com.example.imageEditor.utils.SECRET_KEY

class AuthorizePresenter(private val authorizeRepository: AuthorizeRepository) :
    AuthorizeContract.Presenter {
    private var mView: AuthorizeContract.View? = null

    override fun authorize(authorizationCode: String) {
        val authorizeRequest =
            AuthorizeRequest(
                clientId = ACCESS_KEY,
                clientSecret = SECRET_KEY,
                redirectUri = REDIRECT_URI,
                code = authorizationCode,
            )
        authorizeRepository.authorize(authorizeRequest) {
            mView?.authorized(it)
        }
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: AuthorizeContract.View?) {
        this.mView = view
    }
}
