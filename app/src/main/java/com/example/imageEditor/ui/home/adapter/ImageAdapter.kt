package com.example.imageEditor.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imageEditor.R
import com.example.imageEditor.model.PreviewPhoto
import com.example.imageEditor.model.Urls
import com.example.imageEditor.utils.displayImage

class ImageAdapter(
    private val context: Context,
    private val images: List<PreviewPhoto>,
    private val onClickImage: OnClickImage,
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img: ImageView = view.findViewById(R.id.img)

        fun bind(
            url: Urls,
            onClickImage: OnClickImage,
        ) {
            img.displayImage(url.regular)
            img.setOnClickListener {
                onClickImage.clickImage(url.full)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(images[position].urls, onClickImage)
    }
}
