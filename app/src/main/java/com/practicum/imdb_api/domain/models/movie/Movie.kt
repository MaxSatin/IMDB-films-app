package com.practicum.imdb_api.domain.models.movie

data class Movie(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String,
    val inFavorite: Boolean
)
