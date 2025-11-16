package com.test.pokemon.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import com.test.pokemon.domain.model.Pokemon
import com.test.pokemon.presentation.viewmodel.HomeUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onSearch: (String) -> Unit,
    onPokemonClick: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Discover Pokémon", style = MaterialTheme.typography.headlineSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(
                    text = "Cari, telusuri, dan pelajari kemampuan setiap Pokémon favoritmu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Cari nama Pokémon") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        shape = RoundedCornerShape(24.dp)
                    )
                    FilledTonalButton(
                        onClick = { if (query.isNotBlank()) onSearch(query.trim()) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cari & Lihat Detail")
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isInitialLoading && uiState.pokemons.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    PokemonList(
                        pokemons = uiState.pokemons,
                        isPaginating = uiState.isPaginating,
                        onEndReached = onLoadMore,
                        onPokemonClick = onPokemonClick
                    )

                    if (uiState.errorMessage != null && uiState.pokemons.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(uiState.errorMessage)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onRetry) { Text("Coba Lagi") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonList(
    pokemons: List<Pokemon>,
    isPaginating: Boolean,
    onEndReached: () -> Unit,
    onPokemonClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, pokemons) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { index ->
                if (index != null && index >= pokemons.lastIndex - 2) {
                    onEndReached()
                }
            }
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(pokemons, key = { item -> item.name }) { item ->
            PokemonItem(pokemon = item, onClick = { onPokemonClick(item.name) })
        }
        if (isPaginating) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PokemonItem(pokemon: Pokemon, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "#${pokemon.orderIndex + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = pokemon.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    Text(
                        text = "Tap untuk melihat abilities",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}
