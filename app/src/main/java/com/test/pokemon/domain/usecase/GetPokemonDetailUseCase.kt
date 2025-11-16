package com.test.pokemon.domain.usecase

import com.test.pokemon.domain.repository.PokemonRepository

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(name: String) = repository.getPokemonDetail(name.trim().lowercase())
}
