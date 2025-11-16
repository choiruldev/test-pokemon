package com.test.pokemon.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.pokemon.domain.model.User
import com.test.pokemon.presentation.ui.home.HomeScreen
import com.test.pokemon.presentation.ui.profile.ProfileScreen
import com.test.pokemon.presentation.viewmodel.HomeViewModel

private enum class MainTab(val label: String) {
    HOME("Home"),
    PROFILE("Profile")
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    user: User,
    onPokemonClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    onLogout: () -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text("MobileTask Pokemon") })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                MainTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.label) }
                    )
                }
            }
            when (selectedTab) {
                MainTab.HOME -> HomeScreen(
                    uiState = homeUiState,
                    onLoadMore = homeViewModel::loadNextPage,
                    onRetry = homeViewModel::retry,
                    onSearch = onSearch,
                    onPokemonClick = onPokemonClick
                )
                MainTab.PROFILE -> ProfileScreen(user = user, onLogout = onLogout)
            }
        }
    }
}
