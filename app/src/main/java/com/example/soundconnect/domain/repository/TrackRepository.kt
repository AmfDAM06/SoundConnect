package com.example.soundconnect.domain.repository

import com.example.soundconnect.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTracks(query: String): Result<List<Track>>
    fun getFavorites(): Flow<List<Track>>
    suspend fun toggleFavorite(track: Track)
    suspend fun isFavorite(id: String): Boolean
}
