package com.example.soundconnect.domain.model

data class Track(
    val id: String,
    val name: String,
    val artist: String,
    val imageUrl: String,
    val isFavorite: Boolean = false
)
