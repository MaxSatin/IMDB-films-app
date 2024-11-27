package com.practicum.imdb_api.domain.db

import com.practicum.imdb_api.domain.models.movie.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun historyMovies(): Flow<List<Movie>>
}