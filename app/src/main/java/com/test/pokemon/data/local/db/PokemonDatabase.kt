package com.test.pokemon.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.pokemon.data.local.dao.PokemonDao
import com.test.pokemon.data.local.dao.PokemonDetailDao
import com.test.pokemon.data.local.dao.SessionDao
import com.test.pokemon.data.local.dao.UserDao
import com.test.pokemon.data.local.entity.PokemonDetailEntity
import com.test.pokemon.data.local.entity.PokemonEntity
import com.test.pokemon.data.local.entity.SessionEntity
import com.test.pokemon.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, SessionEntity::class, PokemonEntity::class, PokemonDetailEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonDetailDao(): PokemonDetailDao
}
