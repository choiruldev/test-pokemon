package com.test.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.test.pokemon.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Upsert
    suspend fun upsert(session: SessionEntity)

    @Query("DELETE FROM session")
    suspend fun clear()

    @Query("SELECT * FROM session LIMIT 1")
    fun observeSession(): Flow<SessionEntity?>
}
