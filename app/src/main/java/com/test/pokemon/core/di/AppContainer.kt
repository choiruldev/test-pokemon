package com.test.pokemon.core.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.test.pokemon.data.local.db.PokemonDatabase
import com.test.pokemon.data.remote.service.PokeApiService
import com.test.pokemon.data.repository.AuthRepositoryImpl
import com.test.pokemon.data.repository.PokemonRepositoryImpl
import com.test.pokemon.domain.usecase.FetchPokemonPageUseCase
import com.test.pokemon.domain.usecase.GetPokemonDetailUseCase
import com.test.pokemon.domain.usecase.LoginUserUseCase
import com.test.pokemon.domain.usecase.LogoutUseCase
import com.test.pokemon.domain.usecase.ObserveCurrentUserUseCase
import com.test.pokemon.domain.usecase.ObservePokemonUseCase
import com.test.pokemon.domain.usecase.RegisterUserUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(context: Context) {

    private val database: PokemonDatabase = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        "pokemon-db"
    ).build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

    private val pokeApiService: PokeApiService = retrofit.create(PokeApiService::class.java)

    private val authRepository = AuthRepositoryImpl(
        userDao = database.userDao(),
        sessionDao = database.sessionDao()
    )

    private val pokemonRepository = PokemonRepositoryImpl(
        apiService = pokeApiService,
        pokemonDao = database.pokemonDao(),
        detailDao = database.pokemonDetailDao()
    )

    val registerUserUseCase = RegisterUserUseCase(authRepository)
    val loginUserUseCase = LoginUserUseCase(authRepository)
    val observeCurrentUserUseCase = ObserveCurrentUserUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val observePokemonUseCase = ObservePokemonUseCase(pokemonRepository)
    val fetchPokemonPageUseCase = FetchPokemonPageUseCase(pokemonRepository)
    val getPokemonDetailUseCase = GetPokemonDetailUseCase(pokemonRepository)
}
