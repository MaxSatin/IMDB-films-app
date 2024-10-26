package com.practicum.imdb_api.data.di

import com.practicum.imdb_api.presentation.movie_details.viewmodel.CastInfoViewModel
import com.practicum.imdb_api.presentation.movie_details.viewmodel.MovieDetailsViewModel
import com.practicum.imdb_api.presentation.movie_details.viewmodel.MoviePosterViewModel
import com.practicum.imdb_api.presentation.movies.MoviesSearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesSearchViewModel(androidApplication(), get())
    }

    viewModel { (movieId: String) ->
        MovieDetailsViewModel(movieId, get())
    }

    viewModel { (moviePosterUrl: String) ->
        MoviePosterViewModel(moviePosterUrl)
    }

    viewModel { (movieId: String) ->
        CastInfoViewModel(movieId, get())
    }
}

