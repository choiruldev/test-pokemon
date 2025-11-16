package com.test.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.test.pokemon.PokemonApp
import com.test.pokemon.domain.model.User
import com.test.pokemon.domain.usecase.LogoutUseCase
import com.test.pokemon.domain.usecase.ObserveCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: User) : AuthState
}

class SessionViewModel(
    observeCurrentUser: ObserveCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val authState: StateFlow<AuthState> = observeCurrentUser()
        .map { user -> user?.let { AuthState.Authenticated(it) } ?: AuthState.Unauthenticated }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApp)
                SessionViewModel(
                    observeCurrentUser = app.container.observeCurrentUserUseCase,
                    logoutUseCase = app.container.logoutUseCase
                )
            }
        }
    }
}
