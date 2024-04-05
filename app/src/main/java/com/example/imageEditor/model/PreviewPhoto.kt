package com.example.imageEditor.model

data class PreviewPhoto(
    val assetType: String,
    val blurHash: String,
    val createdAt: String,
    val id: String,
    val slug: String,
    val updatedAt: String,
    val urls: Urls,
)
