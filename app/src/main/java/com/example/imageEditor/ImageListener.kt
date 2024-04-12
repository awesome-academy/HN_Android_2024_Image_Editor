package com.example.imageEditor

import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import kotlin.math.abs

class ImageListener(private val imageView: ImageView) :
    GestureDetector.SimpleOnGestureListener() {
    private var isZoomIn = false
        set(value) {
            field = value
            if (isZoomIn) {
                imageView.animate().scaleX(2.0f).scaleY(2.0f).setDuration(300).start()
            } else {
                imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start()
                imageView.translationX = 0f
                imageView.translationY = 0f
            }
        }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float,
    ): Boolean {
        if (isZoomIn) {
            if (abs(imageView.translationX - distanceX) <= imageView.width / 2) imageView.translationX -= distanceX
            if (abs(imageView.translationY - distanceY) <= imageView.height / 2) imageView.translationY -= distanceY
        }
        return true
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        isZoomIn = !isZoomIn
        return true
    }
}
