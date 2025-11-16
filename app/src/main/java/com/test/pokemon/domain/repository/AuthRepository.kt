package com.test.pokemon.domain.repository

import com.test.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(name: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<User>
    fun observeCurrentUser(): Flow<User?>
    suspend fun logout()
}
