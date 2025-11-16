package com.test.pokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String
)
