package com.example.imageEditor.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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

fun colorFilterList(): List<ColorFilter> {
    val colorFilters = mutableListOf<ColorFilter>()

// Tạo ColorMatrix cho mỗi màu cơ bản và tạo ColorFilter từ ColorMatrix
// Màu đen
    val blackMatrix =
        ColorMatrix().apply {
            setSaturation(0f) // Vô hiệu hóa sắc tố
        }
    colorFilters.add(ColorMatrixColorFilter(blackMatrix))

// Màu đỏ
    val redMatrix =
        ColorMatrix().apply {
            setScale(1f, 0f, 0f, 1f) // Chỉ giữ màu đỏ
        }
    colorFilters.add(ColorMatrixColorFilter(redMatrix))

// Màu xanh lá cây
    val greenMatrix =
        ColorMatrix().apply {
            setScale(0f, 1f, 0f, 1f) // Chỉ giữ màu xanh lá cây
        }
    colorFilters.add(ColorMatrixColorFilter(greenMatrix))

// Màu xanh dương
    val blueMatrix =
        ColorMatrix().apply {
            setScale(0f, 0f, 1f, 1f) // Chỉ giữ màu xanh dương
        }
    colorFilters.add(ColorMatrixColorFilter(blueMatrix))

// Màu trắng
    val whiteMatrix =
        ColorMatrix().apply {
            setSaturation(0f) // Vô hiệu hóa sắc tố
            setScale(1f, 1f, 1f, 1f) // Tất cả thành phần màu giữ nguyên
        }
    colorFilters.add(ColorMatrixColorFilter(whiteMatrix))

// Màu mờ
    val grayscaleMatrix =
        ColorMatrix().apply {
            setSaturation(0f) // Vô hiệu hóa sắc tố
            setScale(0.33f, 0.33f, 0.33f, 1f) // Biến đổi thành màu xám
        }
    colorFilters.add(ColorMatrixColorFilter(grayscaleMatrix))

// Màu âm bản (negative)
    val invertMatrix =
        ColorMatrix().apply {
            set(
                floatArrayOf(
                    -1f,
                    0f,
                    0f,
                    0f,
                    255f,
                    0f,
                    -1f,
                    0f,
                    0f,
                    255f,
                    0f,
                    0f,
                    -1f,
                    0f,
                    255f,
                    0f,
                    0f,
                    0f,
                    1f,
                    0f,
                ),
            )
        }
    colorFilters.add(ColorMatrixColorFilter(invertMatrix))
    return colorFilters
}
