package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.AuthRepository

class LoginUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email.trim().lowercase(), password)
}
