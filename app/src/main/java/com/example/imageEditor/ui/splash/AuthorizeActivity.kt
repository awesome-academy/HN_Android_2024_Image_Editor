package com.example.imageEditor.ui.splash

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityAuthorizeBinding
import com.example.imageEditor.repository.AuthorizeRepository
import com.example.imageEditor.ui.main.MainActivity
import com.example.imageEditor.utils.SIGN_OF_AUTHORIZE
import com.example.imageEditor.utils.authorizeUrl
import com.example.imageEditor.utils.toAuthorizationCode

class AuthorizeActivity : BaseActivity<ActivityAuthorizeBinding>(), AuthorizeContract.View {
    private val mPresenter by lazy { AuthorizePresenter(AuthorizeRepository.getInstance(this)) }

    override fun getViewBinding(): ActivityAuthorizeBinding {
        return ActivityAuthorizeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mPresenter.setView(this)
        binding.webView.loadUrl(authorizeUrl())
        binding.webView.webViewClient =
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    if (request?.url.toString().contains(SIGN_OF_AUTHORIZE)) {
                        mPresenter.authorize(request?.url.toString().toAuthorizationCode())
                    }
                    return false
                }
            }
    }

    override fun initListener() {
    }

    override fun authorized() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
