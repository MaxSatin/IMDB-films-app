package com.practicum.imdb_api.data.dto.movies_search_request

import com.practicum.imdb_api.data.dto.MovieDto
import com.practicum.imdb_api.data.dto.Response

class MoviesSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDto>
) : Response()
