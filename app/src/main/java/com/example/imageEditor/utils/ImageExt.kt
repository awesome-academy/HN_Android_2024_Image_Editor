package com.example.imageEditor.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.displayImage(url: String) {
    Glide.with(this.context).load(url).error(android.R.drawable.stat_notify_error)
        .placeholder(android.R.drawable.stat_sys_download_done).into(this)
}
