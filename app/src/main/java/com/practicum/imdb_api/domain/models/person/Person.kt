package com.practicum.imdb_api.domain.models.person

data class Person(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String,
)
