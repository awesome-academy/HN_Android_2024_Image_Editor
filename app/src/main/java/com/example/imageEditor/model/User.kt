package com.example.imageEditor.model

data class User(
    val acceptedTos: Boolean,
    val bio: String,
    val firstName: String,
    val forHire: Boolean,
    val id: String,
    val instagramUsername: String,
    val lastName: String,
    val links: Links,
    val location: String,
    val name: String,
    val portfolioUrl: String,
    val profileImage: ProfileImage,
    val social: Social,
    val totalCollections: Int,
    val totalLikes: Int,
    val totalPhotos: Int,
    val totalPromotedPhotos: Int,
    val twitterUsername: Any,
    val updatedAt: String,
    val username: String,
)
