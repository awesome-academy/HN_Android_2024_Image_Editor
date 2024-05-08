package com.example.imageEditor.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("source")
    val source: Source? = null,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
)
