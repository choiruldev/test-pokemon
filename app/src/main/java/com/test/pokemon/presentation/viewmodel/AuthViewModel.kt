package com.test.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.test.pokemon.PokemonApp
import com.test.pokemon.domain.usecase.LoginUserUseCase
import com.test.pokemon.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun updateLoginEmail(value: String) {
        _loginUiState.update { it.copy(email = value) }
    }

    fun updateLoginPassword(value: String) {
        _loginUiState.update { it.copy(password = value) }
    }

    fun updateRegisterName(value: String) {
        _registerUiState.update { it.copy(name = value) }
    }

    fun updateRegisterEmail(value: String) {
        _registerUiState.update { it.copy(email = value) }
    }

    fun updateRegisterPassword(value: String) {
        _registerUiState.update { it.copy(password = value) }
    }

    fun updateRegisterConfirmPassword(value: String) {
        _registerUiState.update { it.copy(confirmPassword = value) }
    }

    fun login() {
        val email = _loginUiState.value.email
        val password = _loginUiState.value.password
        if (email.isBlank() || password.isBlank()) {
            _loginUiState.update { it.copy(errorMessage = "Email dan password wajib diisi") }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _loginUiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUserUseCase(email, password)
            _loginUiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun register() {
        val state = _registerUiState.value
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _registerUiState.update { it.copy(errorMessage = "Semua field wajib diisi") }
            return
        }
        if (state.password != state.confirmPassword) {
            _registerUiState.update { it.copy(errorMessage = "Password tidak sama") }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _registerUiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val result = registerUserUseCase(state.name, state.email, state.password)
            _registerUiState.update {
                if (result.isSuccess) {
                    RegisterUiState(successMessage = "Registrasi berhasil, silakan login")
                } else {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun dismissRegisterSuccess() {
        _registerUiState.update { it.copy(successMessage = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApp)
                AuthViewModel(
                    loginUserUseCase = app.container.loginUserUseCase,
                    registerUserUseCase = app.container.registerUserUseCase
                )
            }
        }
    }
}
