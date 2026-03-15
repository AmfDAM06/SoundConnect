package com.example.soundconnect.domain.repository

import com.example.soundconnect.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(text: String): Result<Unit>
}
