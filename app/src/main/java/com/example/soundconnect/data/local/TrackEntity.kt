package com.example.soundconnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class TrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val artist: String,
    val imageUrl: String
)
