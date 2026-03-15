package com.example.soundconnect.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    val currentUser get() = repository.currentUser

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.login(email, password)
            isLoading = false
            if (result.isSuccess) onSuccess() else error = result.exceptionOrNull()?.message
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.register(email, password)
            isLoading = false
            if (result.isSuccess) onSuccess() else error = result.exceptionOrNull()?.message
        }
    }

    fun loginWithCredential(credential: AuthCredential, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.signInWithCredential(credential)
            isLoading = false
            if (result.isSuccess) onSuccess() else error = result.exceptionOrNull()?.message
        }
    }

    fun logout() {
        repository.logout()
    }
}