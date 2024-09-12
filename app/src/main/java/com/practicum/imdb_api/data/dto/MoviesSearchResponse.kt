package com.practicum.imdb_api.data.dto

import com.practicum.imdb_api.domain.models.Movie

class MoviesSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDto>
) : Response()
