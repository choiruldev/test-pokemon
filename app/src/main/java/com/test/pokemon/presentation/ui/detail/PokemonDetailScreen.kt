package com.test.pokemon.presentation.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.test.pokemon.presentation.viewmodel.PokemonDetailUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PokemonDetailScreen(
    uiState: PokemonDetailUiState,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val isNotFound = uiState.errorMessage?.contains("tidak ditemukan", ignoreCase = true) == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.name.ifBlank { "Detail" }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Memuat detail...")
                    }
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isNotFound) "Pokemon tidak ditemukan" else "Terjadi kesalahan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isNotFound) "Coba periksa kembali nama yang kamu masukkan." else uiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (!isNotFound) {
                            Button(onClick = onRetry) {
                                Text("Coba Lagi")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        OutlinedButton(onClick = onBack) {
                            Text("Kembali")
                        }
                    }
                }
                else -> {
                    Text(
                        text = uiState.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Abilities:", style = MaterialTheme.typography.titleMedium)
                    uiState.abilities.forEach { ability ->
                        Text("• ${ability.replaceFirstChar { it.uppercase() }}")
                    }
                }
            }
        }
    }
}
