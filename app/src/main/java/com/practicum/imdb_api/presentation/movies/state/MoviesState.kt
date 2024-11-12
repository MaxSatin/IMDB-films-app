package com.practicum.imdb_api.presentation.movies.state

import com.practicum.imdb_api.domain.models.movie.Movie

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