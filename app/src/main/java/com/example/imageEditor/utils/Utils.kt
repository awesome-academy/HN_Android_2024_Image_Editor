package com.example.imageEditor.utils

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DrawableMarginSpan

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
