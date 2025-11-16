package com.test.pokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo("user_id") val userId: Long
)
