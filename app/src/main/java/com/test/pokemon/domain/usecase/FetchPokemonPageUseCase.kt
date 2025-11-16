package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.PokemonRepository

class FetchPokemonPageUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int, offset: Int) = repository.fetchPokemonPage(limit, offset)
}
