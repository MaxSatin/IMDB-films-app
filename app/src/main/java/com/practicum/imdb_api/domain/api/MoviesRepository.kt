package com.practicum.imdb_api.domain.api

import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}