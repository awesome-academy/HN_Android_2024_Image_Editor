package com.example.imageEditor.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.imageEditor.R
import com.example.imageEditor.base.BaseFragment
import com.example.imageEditor.databinding.FragmentSearchBinding
import com.example.imageEditor.model.PhotoSearchModel
import com.example.imageEditor.repository.SearchRepository
import com.example.imageEditor.ui.detail.ImageDetailActivity
import com.example.imageEditor.ui.search.adapter.SearchImageAdapter
import com.example.imageEditor.utils.DEFAULT_VALUE_ADDED
import com.example.imageEditor.utils.SPAN_COUNT
import com.example.imageEditor.utils.URL
import com.example.imageEditor.utils.setSpanForString

class SearchFragment : BaseFragment<FragmentSearchBinding>(), SearchContract.View {
    private val mPresenter by lazy { SearchPresenter(SearchRepository.getInstance(this)) }
    private val mAdapter by lazy {
        SearchImageAdapter(onClickImage = {
            val intent = Intent(requireContext(), ImageDetailActivity::class.java)
            intent.putExtra(URL, it)
            startActivity(intent)
        })
    }
    private var mPageQuery = 0

    override fun getViewBinding(inflater: LayoutInflater): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        mPresenter.setView(this)
        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            setBackgroundRadioButton(checkedId)
            mAdapter.submitList(null)
            when (checkedId) {
                R.id.rbIgtv -> {
                    mPresenter.searchPhotos(query = getString(R.string.igtv))
                }

                R.id.rbShop -> {
                    mPresenter.searchPhotos(query = getString(R.string.shop))
                }

                R.id.rbStyle -> {
                    mPresenter.searchPhotos(query = getString(R.string.style))
                }

                R.id.rbSports -> {
                    mPresenter.searchPhotos(query = getString(R.string.sports))
                }

                R.id.rbAuto -> {
                    mPresenter.searchPhotos(query = getString(R.string.auto))
                }
            }
        }
        binding?.rbShop?.text =
            setSpanForString(
                getString(R.string.shop),
                resources.getDrawable(R.drawable.ic_shop, null),
            )

        binding?.recycleView?.apply {
            val layoutManager =
                StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS)
            this.layoutManager = layoutManager
            adapter = mAdapter
        }
    }

    override fun initData() {
        mPresenter.searchPhotos()
    }

    override fun initListener() {
        binding?.recycleView?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    val lastVisibleItemPositions =
                        (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                            null,
                        )[0]
                    if ((lastVisibleItemPositions + DEFAULT_VALUE_ADDED) >= totalItemCount) {
                        mPageQuery++
                        mPresenter.searchPhotos(mPageQuery)
                    }
                }
            },
        )
    }

    private fun setBackgroundRadioButton(id: Int) {
        binding?.radioGroup?.forEachIndexed { index, view ->
            view.background =
                if (view.id == id) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_radio_picking,
                    )
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_radio_unpick)
                }
        }
    }

    override fun setupData(data: PhotoSearchModel?) {
        data?.photoModels?.let {
            val newList = mAdapter.currentList.toMutableList()
            newList.addAll(it)
            mAdapter.submitList(newList)
        }
    }
}
