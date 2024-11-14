package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.dto.Response

interface NetworkClient {
    suspend fun  doRequest(dto: Any): Response
}