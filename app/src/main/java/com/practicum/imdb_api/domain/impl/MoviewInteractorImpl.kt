package com.practicum.imdb_api.domain.impl

import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {
        return repository.searchMovies(expression).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun searchPersons(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchPersons(expression).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>> {
        return repository.getMovieDetails(movieId).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun getCastInfo(movieId: String): Flow<Pair<CastInfo?, String?>> {
        return repository.getCastList(movieId).map { result ->
            when(result){
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }
}
