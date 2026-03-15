package com.example.soundconnect.data.repository

import com.example.soundconnect.data.local.TrackDao
import com.example.soundconnect.data.local.TrackEntity
import com.example.soundconnect.data.remote.DeezerApi
import com.example.soundconnect.domain.model.Track
import com.example.soundconnect.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api: DeezerApi,
    private val dao: TrackDao
) : TrackRepository {

    override suspend fun searchTracks(query: String): Result<List<Track>> {
        return try {
            val response = api.searchTracks(query)
            Result.success(response.data.map { dto ->
                Track(
                    id = dto.id,
                    name = dto.title,
                    artist = dto.artist.name,
                    imageUrl = dto.album.cover_medium,
                    isFavorite = dao.isFavorite(dto.id)
                )
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavorites(): Flow<List<Track>> {
        return dao.getFavorites().map { entities ->
            entities.map { Track(it.id, it.name, it.artist, it.imageUrl, true) }
        }
    }

    override suspend fun toggleFavorite(track: Track) {
        val entity = TrackEntity(track.id, track.name, track.artist, track.imageUrl)
        if (dao.isFavorite(track.id)) {
            dao.deleteFavorite(entity)
        } else {
            dao.insertFavorite(entity)
        }
    }

    override suspend fun isFavorite(id: String): Boolean = dao.isFavorite(id)
}
