package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.PokemonRepository

class ObservePokemonUseCase(private val repository: PokemonRepository) {
    operator fun invoke() = repository.observePokemon()
}
