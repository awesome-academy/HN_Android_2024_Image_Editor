package com.example.imageEditor

import android.util.Log
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityMainBinding
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import com.example.imageEditor.ui.MainContract
import com.example.imageEditor.ui.MainPresenter

class MainActivity : BaseActivity<ActivityMainBinding>(), MainContract.View {
    private val mainPresenter by lazy {
        MainPresenter(HomeRepository.getInstance(this))
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mainPresenter.setView(this)
        mainPresenter.getCollections(0)
    }

    override fun initListener() {
    }

    override fun setupCollections(list: List<CollectionModel>) {
        Log.d(">>>>>>>", list.toString())
    }
}
