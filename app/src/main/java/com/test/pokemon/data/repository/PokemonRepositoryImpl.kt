package com.test.pokemon.data.repository

import com.test.pokemon.data.local.dao.PokemonDao
import com.test.pokemon.data.local.dao.PokemonDetailDao
import com.test.pokemon.data.local.entity.PokemonEntity
import com.test.pokemon.data.mapper.toDomain
import com.test.pokemon.data.mapper.toEntity
import com.test.pokemon.data.remote.service.PokeApiService
import com.test.pokemon.domain.model.Pokemon
import com.test.pokemon.domain.model.PokemonDetail
import com.test.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class PokemonRepositoryImpl(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao,
    private val detailDao: PokemonDetailDao
) : PokemonRepository {

    override fun observePokemon(): Flow<List<Pokemon>> =
        pokemonDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun fetchPokemonPage(limit: Int, offset: Int): Result<Boolean> = runCatching {
        val response = apiService.getPokemons(limit, offset)
        val entities = response.results.mapIndexed { index, dto ->
            PokemonEntity(name = dto.name, orderIndex = offset + index)
        }
        if (entities.isNotEmpty()) {
            pokemonDao.upsertAll(entities)
        }
        response.next == null
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> {
        val cached = detailDao.getByName(name)
        return if (cached != null) {
            Result.success(cached.toDomain())
        } else {
            runCatching {
                val entity = apiService.getPokemonDetail(name).toEntity()
                detailDao.upsert(entity)
                entity.toDomain()
            }.recoverCatching { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.code() == 404) {
                            throw IllegalArgumentException("Pokemon tidak ditemukan")
                        } else {
                            throw IllegalStateException("Gagal memuat data (${throwable.code()})")
                        }
                    }
                    is IOException -> throw IOException("Tidak dapat terhubung ke server")
                    else -> throw throwable
                }
            }
        }
    }
}
