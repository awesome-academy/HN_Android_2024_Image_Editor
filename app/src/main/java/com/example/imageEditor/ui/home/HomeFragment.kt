package com.example.imageEditor.ui.home

import android.view.LayoutInflater
import com.example.imageEditor.base.BaseFragment
import com.example.imageEditor.databinding.FragmentHomeBinding
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import com.example.imageEditor.ui.home.adapter.CollectionAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private val mHomePresenter by lazy { HomePresenter(HomeRepository.getInstance(this)) }
    private val mAdapter by lazy { CollectionAdapter() }

    override fun getViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initView() {
        mHomePresenter.setView(this)
        binding?.recycleView?.adapter = mAdapter
    }

    override fun initData() {
        mHomePresenter.getCollections(0)
    }

    override fun setupCollections(list: List<CollectionModel>) {
        mAdapter.submitList(list)
    }
}
