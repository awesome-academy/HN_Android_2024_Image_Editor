package com.example.imageEditor.model

data class CollectionModel(
    val coverPhoto: CoverPhoto,
    val description: Any,
    val featured: Boolean,
    val id: String,
    val lastCollectedAt: String,
    val links: Links,
    val previewPhotos: List<PreviewPhoto>,
    val `private`: Boolean,
    val publishedAt: String,
    val shareKey: String,
    val tags: List<Tag>,
    val title: String,
    val totalPhotos: Int,
    val updatedAt: String,
    val user: User
)
