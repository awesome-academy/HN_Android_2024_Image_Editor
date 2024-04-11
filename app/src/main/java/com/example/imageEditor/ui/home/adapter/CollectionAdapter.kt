package com.example.imageEditor.ui.home.adapter

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.imageEditor.R
import com.example.imageEditor.databinding.ItemCollectionBinding
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.utils.displayImage

class CollectionAdapter : ListAdapter<CollectionModel, CollectionAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CollectionModel>() {
        override fun areItemsTheSame(
            oldItem: CollectionModel,
            newItem: CollectionModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CollectionModel,
            newItem: CollectionModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    },
) {
    private val mChildRecyclerViewState = SparseIntArray()

    class ViewHolder(private val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun mOnScrollListener(
            childRecyclerViewState: SparseIntArray,
            index: Int,
        ): RecyclerView.OnScrollListener {
            return object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        childRecyclerViewState[index] =
                            layoutManager.findLastVisibleItemPosition()
                    }
                }
            }
        }

        fun bindView(
            item: CollectionModel,
            childRecyclerViewState: SparseIntArray,
            index: Int,
        ) {
            binding.imgUser.displayImage(item.user.profileImage.small)
            binding.tvUserName.text = item.user.username
            binding.tvLocation.text = item.user.location
            binding.tvLikes.text =
                binding.root.context.getString(
                    R.string.liked_by_others,
                    item.coverPhoto.likes.toString(),
                )
            binding.tvDescription.text = item.descriptionTextShow

            binding.recycleViewImg.setHasFixedSize(true)
            val adapter = ImageAdapter(binding.root.context, item.previewPhotos)
            binding.recycleViewImg.adapter = adapter

            if (childRecyclerViewState.containsKey(index)) {
                val position = childRecyclerViewState[index]
                binding.recycleViewImg.scrollToPosition(position)
            }
            if (binding.recycleViewImg.onFlingListener == null) {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding.recycleViewImg)
            }
            binding.recycleViewImg.clearOnScrollListeners()
            binding.recycleViewImg.addOnScrollListener(
                mOnScrollListener(
                    childRecyclerViewState,
                    index,
                ),
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bindView(getItem(position), mChildRecyclerViewState, position)
    }
}
