package com.test.pokemon.data.repository

import com.test.pokemon.data.local.dao.SessionDao
import com.test.pokemon.data.local.dao.UserDao
import com.test.pokemon.data.local.entity.SessionEntity
import com.test.pokemon.data.local.entity.UserEntity
import com.test.pokemon.data.mapper.toDomain
import com.test.pokemon.domain.model.User
import com.test.pokemon.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val sessionDao: SessionDao
) : AuthRepository {

    override suspend fun registerUser(name: String, email: String, password: String): Result<Unit> = runCatching {
        val existing = userDao.getByEmail(email)
        require(existing == null) { "Email sudah terdaftar" }
        val entity = UserEntity(name = name, email = email, password = password)
        userDao.insert(entity)
    }.map { }

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        val user = userDao.getByEmail(email) ?: error("User tidak ditemukan")
        require(user.password == password) { "Password salah" }
        sessionDao.upsert(SessionEntity(userId = user.id))
        user.toDomain()
    }

    override fun observeCurrentUser(): Flow<User?> =
        sessionDao.observeSession().mapLatest { session ->
            session?.let { current -> userDao.getById(current.userId)?.toDomain() }
        }

    override suspend fun logout() {
        sessionDao.clear()
    }
}
