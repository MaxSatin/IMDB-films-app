package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.dto.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.MoviesSearchResponse
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as MoviesSearchResponse).results.map{
                    Movie(it.id, it.resultType, it.image, it.title, it.description)})
                }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}