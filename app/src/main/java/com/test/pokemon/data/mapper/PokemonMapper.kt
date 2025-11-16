package com.test.pokemon.data.mapper

import com.test.pokemon.data.local.entity.PokemonDetailEntity
import com.test.pokemon.data.local.entity.PokemonEntity
import com.test.pokemon.data.remote.dto.PokemonDetailResponse
import com.test.pokemon.domain.model.Pokemon
import com.test.pokemon.domain.model.PokemonDetail

fun PokemonEntity.toDomain() = Pokemon(name = name, orderIndex = orderIndex)

fun PokemonDetailEntity.toDomain() = PokemonDetail(name = name, abilities = abilities)

fun PokemonDetailResponse.toEntity() =
    PokemonDetailEntity(
        name = name,
        abilities = abilities.map { it.ability.name }
    )
