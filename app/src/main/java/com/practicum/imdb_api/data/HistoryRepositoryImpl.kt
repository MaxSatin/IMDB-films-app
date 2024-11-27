package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.converters.MovieDbConvertor
import com.practicum.imdb_api.data.db.AppDataBase
import com.practicum.imdb_api.data.db.entity.MovieEntity
import com.practicum.imdb_api.domain.db.HistoryRepository
import com.practicum.imdb_api.domain.models.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val movieDbConvertor: MovieDbConvertor
): HistoryRepository {
    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDataBase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>):List<Movie> {
        return movies.map {
            movie -> movieDbConvertor.map(movie)
        }
    }
}