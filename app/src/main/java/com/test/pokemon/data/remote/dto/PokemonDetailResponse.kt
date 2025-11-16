package com.test.pokemon.data.remote.dto

data class PokemonDetailResponse(
    val name: String,
    val abilities: List<PokemonAbilityWrapper>
)

data class PokemonAbilityWrapper(
    val ability: PokemonAbility
)

data class PokemonAbility(
    val name: String
)
