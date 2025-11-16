package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
