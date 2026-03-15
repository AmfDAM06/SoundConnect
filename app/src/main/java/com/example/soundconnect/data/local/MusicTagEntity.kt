package com.example.soundconnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_tags")
data class MusicTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
