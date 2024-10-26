package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
    fun getCastList(movieId: String): Resource<CastInfo>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}