package com.example.imageEditor.ui.home

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageEditor.base.BaseFragment
import com.example.imageEditor.databinding.FragmentHomeBinding
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import com.example.imageEditor.ui.detail.ImageDetailActivity
import com.example.imageEditor.ui.home.adapter.CollectionAdapter
import com.example.imageEditor.ui.home.adapter.OnClickImage
import com.example.imageEditor.utils.URL

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View, OnClickImage {
    private val mHomePresenter by lazy { HomePresenter(HomeRepository.getInstance(this)) }
    private val mAdapter by lazy { CollectionAdapter(this) }
    private var mPageQuery = 0

    override fun getViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initView() {
        mHomePresenter.setView(this)
        binding?.recycleView?.adapter = mAdapter
        binding?.recycleView?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager: LinearLayoutManager =
                        recyclerView.layoutManager as LinearLayoutManager
                    if (dy > 0 && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.currentList.size - 1) {
                        mPageQuery++
                        mHomePresenter.getCollections(mPageQuery)
                    }
                }
            },
        )
    }

    override fun initData() {
        mHomePresenter.getCollections(mPageQuery)
    }

    override fun setupCollections(list: List<CollectionModel>) {
        val newList = mAdapter.currentList.toMutableList()
        newList.addAll(list)
        mAdapter.submitList(newList)
    }

    override fun clickImage(url: String) {
        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        intent.putExtra(URL, url)
        startActivity(intent)
    }
}
