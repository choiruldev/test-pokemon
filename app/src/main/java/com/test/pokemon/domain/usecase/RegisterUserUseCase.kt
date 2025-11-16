package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.AuthRepository

class RegisterUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        repository.registerUser(name.trim(), email.trim().lowercase(), password)
}
