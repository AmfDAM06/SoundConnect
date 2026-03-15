package com.example.soundconnect.data.repository

import com.example.soundconnect.domain.model.ChatMessage
import com.example.soundconnect.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override fun getMessages(): Flow<List<ChatMessage>> = callbackFlow {
        val listener = firestore.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(text: String): Result<Unit> = try {
        val user = auth.currentUser ?: throw Exception("Not authenticated")
        val message = ChatMessage(
            id = firestore.collection("messages").document().id,
            senderId = user.uid,
            senderEmail = user.email ?: "Unknown",
            text = text
        )
        firestore.collection("messages").document(message.id).set(message).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
