package com.test.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.test.pokemon.data.local.entity.PokemonDetailEntity

@Dao
interface PokemonDetailDao {
    @Upsert
    suspend fun upsert(detail: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_details WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): PokemonDetailEntity?
}
