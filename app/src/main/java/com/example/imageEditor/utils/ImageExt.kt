package com.example.imageEditor.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.displayImage(
    url: String,
    onSuccess: (Bitmap) -> Unit = {},
) {
    Glide.with(this.context).load(url).error(android.R.drawable.stat_notify_error)
        .placeholder(android.R.drawable.stat_sys_download_done)
        .listener(
            object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    if (width > 0 && height > 0) onSuccess(resource.toBitmap(width, height))
                    return false
                }
            },
        ).into(this)
}
