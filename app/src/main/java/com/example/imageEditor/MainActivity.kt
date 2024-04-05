package com.example.imageEditor

import android.util.Log
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityMainBinding
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import com.example.imageEditor.ui.HomeContract
import com.example.imageEditor.ui.HomePresenter

class MainActivity : BaseActivity<ActivityMainBinding>(), HomeContract.View {
    private val homePresenter by lazy {
        HomePresenter(HomeRepository.getInstance(this))
    }

    override fun initListener() {
    }

    override fun initView() {
        homePresenter.setView(this)
        homePresenter.getCollections(0)
    }

    override fun getContentLayout() = R.layout.activity_main

    override fun setupCollections(list: List<CollectionModel>) {
        Log.e(">>>>>>>", list.toString())
    }
}
