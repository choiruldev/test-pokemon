package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.AuthRepository

class ObserveCurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.observeCurrentUser()
}
