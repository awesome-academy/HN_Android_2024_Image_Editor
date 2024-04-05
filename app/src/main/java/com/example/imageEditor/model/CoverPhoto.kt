package com.example.imageEditor.model

data class CoverPhoto(
    val altDescription: String,
    val alternativeSlugs: AlternativeSlugs,
    val assetType: String,
    val blurHash: String,
    val breadcrumbs: List<Any>,
    val color: String,
    val createdAt: String,
    val currentUserCollections: List<Any>,
    val description: String,
    val height: Int,
    val id: String,
    val likedByUser: Boolean,
    val likes: Int,
    val links: Links,
    val promotedAt: Any,
    val slug: String,
    val sponsorship: Any,
    val topicSubmissions: TopicSubmissions,
    val updatedAt: String,
    val urls: Urls,
    val user: User,
    val width: Int
)
