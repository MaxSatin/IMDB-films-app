package com.practicum.imdb_api.domain.models

data class Movie(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String
)
