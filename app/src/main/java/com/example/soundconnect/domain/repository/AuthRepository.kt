package com.example.soundconnect.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, pass: String): Result<FirebaseUser>
    suspend fun register(email: String, pass: String): Result<FirebaseUser>
    suspend fun signInWithCredential(credential: AuthCredential): Result<FirebaseUser>
    fun logout()
}