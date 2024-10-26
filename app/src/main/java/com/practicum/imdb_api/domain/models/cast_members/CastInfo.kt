package com.practicum.imdb_api.domain.models.cast_members

data class CastInfo(
    val id: String,
    val title: String,
    val fullTitle: String,
    val type: String,
    val year: String,
    val directors: CastShort,
    val writers: CastShort,
    val actors: List<ActorShort>,
)

class CastShort(
    val job: String,
    val items: List<CastShortItem>
)