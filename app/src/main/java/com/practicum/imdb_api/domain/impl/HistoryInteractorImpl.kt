package com.practicum.imdb_api.domain.impl

import com.practicum.imdb_api.data.converters.MovieDbConvertor
import com.practicum.imdb_api.domain.db.HistoryInteractor
import com.practicum.imdb_api.domain.db.HistoryRepository
import com.practicum.imdb_api.domain.models.movie.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository,
): HistoryInteractor {

    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }
}