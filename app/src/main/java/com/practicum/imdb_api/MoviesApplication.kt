package com.practicum.imdb_api

import android.app.Application
import com.practicum.imdb_api.presentation.movies.MoviesSearchPresenter

class MoviesApplication: Application() {

    var moviesSearchPresenter: MoviesSearchPresenter? = null
}