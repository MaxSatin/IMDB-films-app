package com.practicum.imdb_api

import android.app.Application
import com.practicum.imdb_api.data.di.dataModule
import com.practicum.imdb_api.data.di.interactorModule
import com.practicum.imdb_api.data.di.navigationModule
import com.practicum.imdb_api.data.di.viewModelModule
import com.practicum.imdb_api.presentation.movies.MoviesSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {

//    var moviesSearchPresenter: MoviesSearchViewModel? = null

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MoviesApplication)
            modules(dataModule, interactorModule, viewModelModule, navigationModule)
        }
    }
}