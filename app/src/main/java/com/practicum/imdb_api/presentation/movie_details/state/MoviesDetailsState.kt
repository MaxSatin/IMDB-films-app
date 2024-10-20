package com.practicum.imdb_api.presentation.movie_details.state

import com.practicum.imdb_api.domain.models.MovieDetails

interface MoviesDetailsState {
    object Loading: MoviesDetailsState
    data class Content(
        val moviesDetails: MovieDetails
    ): MoviesDetailsState

    data class Error(
        val errorMessage: String
    ): MoviesDetailsState

    data class Empty(
        val message: String
    ): MoviesDetailsState
}