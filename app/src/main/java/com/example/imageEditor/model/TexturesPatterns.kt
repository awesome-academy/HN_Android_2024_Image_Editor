package com.example.imageEditor.model

import com.google.gson.annotations.SerializedName

data class TexturesPatterns(
    @SerializedName("approved_on")
    val approvedOn: String,
    @SerializedName("status")
    val status: String,
)