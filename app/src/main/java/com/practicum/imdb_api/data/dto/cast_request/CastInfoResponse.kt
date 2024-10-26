package com.practicum.imdb_api.data.dto.cast_request

import com.practicum.imdb_api.data.dto.cast_search_request.ActorShort
import com.practicum.imdb_api.data.dto.cast_search_request.CastShortItem
import com.practicum.imdb_api.data.dto.Response

data class CastInfoResponse(
    val imDbId: String,
    val title: String,
    val fullTitle: String,
    val type: String,
    val year: String,
    val directors: CastShort,
    val writers: CastShort,
    val actors: List<ActorShort>,
): Response()

