package com.example.imageEditor.ui.detail

import android.annotation.SuppressLint
import android.view.GestureDetector
import com.example.imageEditor.ImageListener
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityDetailImageBinding
import com.example.imageEditor.repository.DetailRepository
import com.example.imageEditor.utils.URL
import com.example.imageEditor.utils.displayImage

class ImageDetailActivity : BaseActivity<ActivityDetailImageBinding>() {
    private val imageDetailPresenter by lazy { ImageDetailPresenter(DetailRepository.getInstance()) }
    private val url by lazy { intent.getStringExtra(URL) }
    private val imageListener by lazy { ImageListener(binding.img) }
    private val scaleGestureDetector by lazy {
        GestureDetector(
            this,
            imageListener,
        )
    }

    override fun getViewBinding(): ActivityDetailImageBinding {
        return ActivityDetailImageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        url?.let { binding.img.displayImage(it) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        scaleGestureDetector.setOnDoubleTapListener(imageListener)
        binding.imgDownLoad.setOnClickListener {
            url?.let {
                imageDetailPresenter.downloadImage(it)
            }
        }
        binding.img.setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)
            view.performClick()
            true
        }
    }
}
