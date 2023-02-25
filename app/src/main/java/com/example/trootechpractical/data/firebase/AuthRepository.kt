package com.example.trootechpractical.data.firebase

import com.example.trootechpractical.domain.common.Output
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Output<FirebaseUser>
    suspend fun signup(
        name: String,
        email: String,
        password: String,
        fileUri: String?
    ): Output<FirebaseUser>

    fun logout()
}