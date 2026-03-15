package com.example.soundconnect.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class, MusicTagEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val trackDao: TrackDao
}
