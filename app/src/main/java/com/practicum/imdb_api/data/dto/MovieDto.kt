package com.practicum.imdb_api.data.dto

data class MovieDto(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String
)