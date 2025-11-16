package com.test.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.test.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Upsert
    suspend fun upsertAll(items: List<PokemonEntity>)

    @Query("SELECT * FROM pokemons ORDER BY order_index ASC")
    fun observeAll(): Flow<List<PokemonEntity>>

    @Query("SELECT COUNT(*) FROM pokemons")
    suspend fun count(): Int
}
