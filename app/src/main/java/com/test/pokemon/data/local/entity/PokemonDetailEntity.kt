package com.test.pokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_details")
data class PokemonDetailEntity(
    @PrimaryKey val name: String,
    @ColumnInfo("abilities") val abilities: List<String>
)
