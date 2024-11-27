package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.domain.models.person.Person
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>>
    fun searchPersons(expression: String): Flow<Pair<List<Person>?, String?>>
    fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>
    fun getCastInfo(movieId: String): Flow<Pair<CastInfo?, String?>>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    interface MovieDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }

    interface  CastInfoConsumer {
        fun consume(castInfo: CastInfo?, errorMessage: String?)
    }

    interface PersonsConsumer {
        fun consume(foundPersons: List<Person>?, errorMessage: String?)
    }
}