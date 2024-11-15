package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(expression: String): Flow<Resource<List<Movie>>>
    fun searchPersons(expression: String): Flow<Resource<List<Person>>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
    fun getCastList(movieId: String): Resource<CastInfo>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}