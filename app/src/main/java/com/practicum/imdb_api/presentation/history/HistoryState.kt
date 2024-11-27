package com.practicum.imdb_api.presentation.history

import com.practicum.imdb_api.domain.impl.HistoryInteractorImpl
import com.practicum.imdb_api.domain.models.movie.Movie

sealed interface HistoryState {
    object Loading: HistoryState

    data class Content(
        val movies: List<Movie>
    ): HistoryState

    data class Empty(
        val message: String
    ): HistoryState
}