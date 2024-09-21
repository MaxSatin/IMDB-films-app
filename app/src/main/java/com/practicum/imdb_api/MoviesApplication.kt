package com.practicum.imdb_api

import android.app.Application
import com.practicum.imdb_api.presentation.movies.MoviesSearchViewModel

class MoviesApplication: Application() {

    var moviesSearchPresenter: MoviesSearchViewModel? = null
}