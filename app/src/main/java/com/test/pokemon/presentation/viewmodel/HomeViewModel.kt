package com.test.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.test.pokemon.PokemonApp
import com.test.pokemon.domain.model.Pokemon
import com.test.pokemon.domain.usecase.FetchPokemonPageUseCase
import com.test.pokemon.domain.usecase.ObservePokemonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PAGE_LIMIT = 10

data class HomeUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val endReached: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    observePokemonUseCase: ObservePokemonUseCase,
    private val fetchPokemonPageUseCase: FetchPokemonPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isInitialLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observePokemonUseCase().collectLatest { list ->
                _uiState.update { current ->
                    current.copy(
                        pokemons = list,
                        isInitialLoading = current.isInitialLoading && list.isEmpty(),
                        errorMessage = null
                    )
                }
            }
        }
        loadNextPage()
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isInitialLoading.not() && state.isPaginating.not() && state.endReached) return
        if (state.isPaginating) return
        viewModelScope.launch(Dispatchers.IO) {
            val initialLoad = _uiState.value.pokemons.isEmpty()
            _uiState.update {
                it.copy(
                    isInitialLoading = initialLoad,
                    isPaginating = !initialLoad,
                    errorMessage = null
                )
            }
            val offset = _uiState.value.pokemons.size
            val result = fetchPokemonPageUseCase(PAGE_LIMIT, offset)
            _uiState.update {
                if (result.isSuccess) {
                    val endReached = result.getOrNull() == true
                    it.copy(
                        isInitialLoading = false,
                        isPaginating = false,
                        endReached = endReached
                    )
                } else {
                    it.copy(
                        isInitialLoading = false,
                        isPaginating = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Gagal memuat data"
                    )
                }
            }
        }
    }

    fun retry() {
        loadNextPage()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApp)
                HomeViewModel(
                    observePokemonUseCase = app.container.observePokemonUseCase,
                    fetchPokemonPageUseCase = app.container.fetchPokemonPageUseCase
                )
            }
        }
    }
}
