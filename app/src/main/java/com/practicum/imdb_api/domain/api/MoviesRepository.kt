package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.api.MoviesInteractor.MovieDetailsConsumer
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.domain.models.MovieDetails
import com.practicum.imdb_api.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}