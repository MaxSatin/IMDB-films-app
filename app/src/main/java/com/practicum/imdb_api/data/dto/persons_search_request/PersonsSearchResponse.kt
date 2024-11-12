package com.practicum.imdb_api.data.dto.persons_search_request

import com.practicum.imdb_api.data.dto.Response

data class PersonsSearchResponse (
    val searchType: String,
    val expression: String,
    val results: List<PersonDto>
): Response()
