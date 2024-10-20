package com.practicum.imdb_api.data

import com.practicum.imdb_api.data.Storage.LocalStorage
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsRequest
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchResponse
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.domain.models.MovieDetails
import com.practicum.imdb_api.util.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                val stored = localStorage.getSavedFavorites()
                Resource.Success((response as MoviesSearchResponse).results.map{
                    Movie(it.id, it.resultType, it.image, it.title, it.description, inFavorite = stored.contains(it.id))})
                }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieDetails(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MoviesDetailsRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success(response as MovieDetails)
            }
            else -> Resource.Error("Ошибка сервера")
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }
}