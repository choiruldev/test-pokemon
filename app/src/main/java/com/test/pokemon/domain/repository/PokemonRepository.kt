package com.test.pokemon.domain.repository

import com.test.pokemon.domain.model.Pokemon
import com.test.pokemon.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun observePokemon(): Flow<List<Pokemon>>
    suspend fun fetchPokemonPage(limit: Int, offset: Int): Result<Boolean>
    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>
}
