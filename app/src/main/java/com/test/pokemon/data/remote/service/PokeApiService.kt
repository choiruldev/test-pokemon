package com.test.pokemon.data.remote.service

import com.test.pokemon.data.remote.dto.PokemonDetailResponse
import com.test.pokemon.data.remote.dto.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailResponse
}
