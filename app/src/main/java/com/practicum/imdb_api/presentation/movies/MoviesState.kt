package com.practicum.imdb_api.presentation.movies

import com.practicum.imdb_api.domain.models.Movie

sealed interface MoviesState {

    object Loading: MoviesState

    data class Content(
        val movies: List<Movie>
    ): MoviesState

    data class Error(
        val errorMessage: String
    ): MoviesState

    data class Empty(
        val message: String
    ): MoviesState
}