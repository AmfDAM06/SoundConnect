package com.example.soundconnect.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.domain.model.ChatMessage
import com.example.soundconnect.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    val messages = repository.getMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var messageText by mutableStateOf("")
    val currentUserId = auth.currentUser?.uid

    fun sendMessage() {
        if (messageText.isBlank()) return
        val text = messageText
        messageText = ""
        viewModelScope.launch {
            repository.sendMessage(text)
        }
    }
}
