package com.example.soundconnect.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.domain.repository.AuthRepository
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
    var isAuth by mutableStateOf(repository.currentUser != null)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            repository.login(email, password)
                .onSuccess { isAuth = true; onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            repository.register(email, password)
                .onSuccess { isAuth = true; onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }
}