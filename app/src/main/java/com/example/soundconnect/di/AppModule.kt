package com.example.soundconnect.di

import android.app.Application
import androidx.room.Room
import com.example.soundconnect.data.local.AppDatabase
import com.example.soundconnect.data.local.TrackDao
import com.example.soundconnect.data.remote.DeezerApi
import com.example.soundconnect.data.repository.AuthRepositoryImpl
import com.example.soundconnect.data.repository.ChatRepositoryImpl
import com.example.soundconnect.data.repository.TrackRepositoryImpl
import com.example.soundconnect.domain.repository.AuthRepository
import com.example.soundconnect.domain.repository.ChatRepository
import com.example.soundconnect.domain.repository.TrackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideChatRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): ChatRepository {
        return ChatRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideDeezerApi(): DeezerApi {
        return Retrofit.Builder()
            .baseUrl(DeezerApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeezerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "soundconnect_db").build()
    }

    @Provides
    @Singleton
    fun provideTrackDao(db: AppDatabase): TrackDao {
        return db.trackDao
    }

    @Provides
    @Singleton
    fun provideRepository(api: DeezerApi, trackDao: TrackDao): TrackRepository {
        return TrackRepositoryImpl(api, trackDao)
    }
}