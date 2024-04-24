package com.example.imageEditor.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DrawableMarginSpan
import androidx.camera.core.ImageProxy

fun setSpanForString(
    text: String,
    drawable: Drawable,
): SpannableString {
    val spannableString = SpannableString(text)
    spannableString.setSpan(
        DrawableMarginSpan(drawable, 20),
        0,
        spannableString.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
    )
    return spannableString
}

fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
