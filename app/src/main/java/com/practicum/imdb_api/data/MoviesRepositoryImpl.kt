package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.dto.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.MoviesSearchResponse
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.Movie

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): List<Movie> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as MoviesSearchResponse).results.map {
                Movie(it.id, it.resultType, it.image, it.title, it.description) }
        } else {
            return emptyList()
        }
    }
}