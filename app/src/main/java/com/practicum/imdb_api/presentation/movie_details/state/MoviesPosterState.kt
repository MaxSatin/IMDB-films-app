package com.practicum.imdb_api.presentation.movie_details.state

sealed interface MoviesPosterState {
    object Loading: MoviesPosterState

    data class Content(
        val moviesPosterUrl: String
    ): MoviesPosterState

    data class Error(
        val errorMessage: String
    ): MoviesPosterState

    data class Empty(
        val message: String
    ): MoviesPosterState
}