package com.test.pokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey val name: String,
    @ColumnInfo("order_index") val orderIndex: Int
)
