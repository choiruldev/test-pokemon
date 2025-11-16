package com.test.pokemon.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.test.pokemon.presentation.viewmodel.LoginUiState

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Selamat Datang Kembali",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Masuk untuk melanjutkan perjalanan Pokémon-mu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        singleLine = true,
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(18.dp)
                    )
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password") },
                        singleLine = true,
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (showPassword) "Sembunyikan password" else "Tampilkan password"
                                )
                            }
                        },
                        shape = RoundedCornerShape(18.dp)
                    )
                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = onLogin,
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(if (uiState.isLoading) "Memproses..." else "Masuk")
                    }
                    OutlinedButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Belum punya akun? Daftar")
                    }
                }
            }
        }
    }
}
