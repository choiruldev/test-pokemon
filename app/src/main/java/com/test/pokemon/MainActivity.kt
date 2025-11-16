package com.test.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.test.pokemon.presentation.navigation.PokemonNavHost
import com.test.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonRoot()
        }
    }
}

@Composable
fun PokemonRoot() {
    PokemonTheme {
        Surface {
            PokemonNavHost()
        }
    }
}
