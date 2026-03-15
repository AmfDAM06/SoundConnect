package com.example.soundconnect.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(track: TrackEntity)

    @Delete
    suspend fun deleteFavorite(track: TrackEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert
    suspend fun insertMusicTag(tag: MusicTagEntity)

    @Query("SELECT * FROM music_tags")
    fun getAllMusicTags(): Flow<List<MusicTagEntity>>
}
