package com.test.pokemon

import android.app.Application
import com.test.pokemon.core.di.AppContainer

class PokemonApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
