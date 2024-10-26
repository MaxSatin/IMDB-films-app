package com.practicum.imdb_api.data.dto.cast_request

import com.practicum.imdb_api.data.dto.cast_search_request.CastShortItem

data class CastShort(
    val job: String,
    val items: List<CastShortItem>
)