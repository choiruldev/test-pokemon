package com.test.pokemon.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.pokemon.domain.model.User
import com.test.pokemon.presentation.ui.MainScreen
import com.test.pokemon.presentation.ui.auth.LoginScreen
import com.test.pokemon.presentation.ui.auth.RegisterScreen
import com.test.pokemon.presentation.ui.detail.PokemonDetailScreen
import com.test.pokemon.presentation.viewmodel.AuthState
import com.test.pokemon.presentation.viewmodel.AuthViewModel
import com.test.pokemon.presentation.viewmodel.DETAIL_NAME_KEY
import com.test.pokemon.presentation.viewmodel.PokemonDetailViewModel
import com.test.pokemon.presentation.viewmodel.SessionViewModel

private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val MAIN_ROUTE = "main"
private const val DETAIL_ROUTE = "detail"

@Composable
fun PokemonNavHost() {
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModel.Factory)
    val authState by sessionViewModel.authState.collectAsStateWithLifecycle()

    when (val state = authState) {
        AuthState.Loading -> LoadingScreen()
        AuthState.Unauthenticated -> AuthNavigator()
        is AuthState.Authenticated -> MainNavigator(
            user = state.user,
            onLogout = sessionViewModel::logout
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun AuthNavigator() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val loginState by authViewModel.loginUiState.collectAsStateWithLifecycle()
    val registerState by authViewModel.registerUiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                uiState = loginState,
                onEmailChange = authViewModel::updateLoginEmail,
                onPasswordChange = authViewModel::updateLoginPassword,
                onLogin = authViewModel::login,
                onNavigateToRegister = { navController.navigate(REGISTER_ROUTE) }
            )
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                uiState = registerState,
                onNameChange = authViewModel::updateRegisterName,
                onEmailChange = authViewModel::updateRegisterEmail,
                onPasswordChange = authViewModel::updateRegisterPassword,
                onConfirmPasswordChange = authViewModel::updateRegisterConfirmPassword,
                onRegister = authViewModel::register,
                onNavigateBack = { navController.popBackStack() },
                onDismissSuccess = authViewModel::dismissRegisterSuccess
            )
        }
    }
}

@Composable
private fun MainNavigator(user: User, onLogout: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MAIN_ROUTE) {
        composable(MAIN_ROUTE) {
            MainScreen(
                user = user,
                onPokemonClick = { name -> navController.navigate("$DETAIL_ROUTE/$name") },
                onSearch = { query -> navController.navigate("$DETAIL_ROUTE/${query.trim().lowercase()}") },
                onLogout = onLogout
            )
        }
        composable(
            route = "$DETAIL_ROUTE/{$DETAIL_NAME_KEY}",
            arguments = listOf(navArgument(DETAIL_NAME_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString(DETAIL_NAME_KEY).orEmpty()
            val viewModel: PokemonDetailViewModel = viewModel(
                factory = PokemonDetailViewModel.provideFactory(pokemonName),
                viewModelStoreOwner = backStackEntry
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PokemonDetailScreen(
                uiState = uiState,
                onRetry = viewModel::loadPokemon,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
