package com.example.soundconnect.domain.model

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderEmail: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
