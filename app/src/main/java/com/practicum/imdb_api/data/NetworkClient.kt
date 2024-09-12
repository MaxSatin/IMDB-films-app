package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}