package com.test.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.pokemon.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): UserEntity?
}
