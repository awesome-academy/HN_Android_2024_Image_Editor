package com.example.imageEditor.ui.main

import com.example.imageEditor.R
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityMainBinding
import com.example.imageEditor.ui.create.CreateFragment
import com.example.imageEditor.ui.favourite.FavouriteFragment
import com.example.imageEditor.ui.home.HomeFragment
import com.example.imageEditor.ui.search.SearchFragment
import com.example.imageEditor.utils.CREATE_INDEX
import com.example.imageEditor.utils.FAVORITE_INDEX
import com.example.imageEditor.utils.HOME_INDEX
import com.example.imageEditor.utils.SEARCH_INDEX

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mMainViewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager, lifecycle) }
    private val mHomeFragment by lazy { HomeFragment() }
    private val mSearchFragment by lazy { SearchFragment() }
    private val mCreateFragment by lazy { CreateFragment() }
    private val mFavouriteFragment by lazy { FavouriteFragment() }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setupPager()
    }

    override fun initListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.pager2.currentItem = HOME_INDEX
                    true
                }

                R.id.search -> {
                    binding.pager2.currentItem = SEARCH_INDEX
                    true
                }

                R.id.add -> {
                    binding.pager2.currentItem = CREATE_INDEX
                    true
                }

                R.id.favourite -> {
                    binding.pager2.currentItem = FAVORITE_INDEX
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setupPager() {
        mMainViewPagerAdapter.addFragment(mHomeFragment)
        mMainViewPagerAdapter.addFragment(mSearchFragment)
        mMainViewPagerAdapter.addFragment(mCreateFragment)
        mMainViewPagerAdapter.addFragment(mFavouriteFragment)
        binding.pager2.offscreenPageLimit = mMainViewPagerAdapter.itemCount
        binding.pager2.adapter = mMainViewPagerAdapter
        binding.pager2.isUserInputEnabled = false // disable swiping
    }
}
