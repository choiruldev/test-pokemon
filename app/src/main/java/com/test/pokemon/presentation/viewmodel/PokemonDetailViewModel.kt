package com.test.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.test.pokemon.PokemonApp
import com.test.pokemon.domain.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DETAIL_NAME_KEY = "pokemonName"

data class PokemonDetailUiState(
    val name: String = "",
    val abilities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PokemonDetailViewModel(
    private val pokemonName: String,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        if (pokemonName.isNotBlank()) {
            loadPokemon()
        } else {
            _uiState.update { it.copy(errorMessage = "Nama pokemon tidak ditemukan") }
        }
    }

    fun loadPokemon() {
        if (pokemonName.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(name = pokemonName, isLoading = true, errorMessage = null) }
            val result = getPokemonDetailUseCase(pokemonName)
            _uiState.update {
                if (result.isSuccess) {
                    val detail = result.getOrThrow()
                    it.copy(
                        name = detail.name,
                        abilities = detail.abilities,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Gagal memuat detail"
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(pokemonName: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApp)
                    PokemonDetailViewModel(
                        pokemonName = pokemonName,
                        getPokemonDetailUseCase = app.container.getPokemonDetailUseCase
                    )
                }
            }
    }
}
