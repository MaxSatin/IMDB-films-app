package com.practicum.imdb_api.data.dto.movies_details_request

import com.practicum.imdb_api.data.dto.Response

data class MoviesDetailsResponse(
    val id: String,
    val title: String,
    val imDbRating: String,
    val year: String,
    val countries: String,
    val genres: String,
    val directors: String,
    val writers: String,
    val stars: String,
    val plot: String
) : Response()