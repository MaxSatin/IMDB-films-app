package com.practicum.imdb_api.data.dto.persons_search_request

data class PersonDto(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String
)