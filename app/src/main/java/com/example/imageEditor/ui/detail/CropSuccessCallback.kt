package com.example.imageEditor.ui.detail

import android.graphics.PointF

interface CropSuccessCallback {
    fun onCropSuccess(
        startPointF: PointF,
        endPointF: PointF,
    )
}
