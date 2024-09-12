package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}