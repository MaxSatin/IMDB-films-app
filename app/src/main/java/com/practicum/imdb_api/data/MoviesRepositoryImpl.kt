package com.practicum.imdb_api.data

import android.util.Log
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.cast_members.CastShort
import com.practicum.imdb_api.data.Storage.LocalStorage
import com.practicum.imdb_api.data.dto.cast_request.CastInfoRequest
import com.practicum.imdb_api.data.dto.cast_request.CastInfoResponse
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsRequest
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsResponse
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchResponse
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.cast_members.ActorShort
import com.practicum.imdb_api.domain.models.cast_members.CastShortItem
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.util.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()
                Resource.Success((response as MoviesSearchResponse).results.map {
                    Movie(
                        it.id,
                        it.resultType,
                        it.image,
                        it.title,
                        it.description,
                        inFavorite = stored.contains(it.id)
                    )
                })
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
                response as MoviesDetailsResponse
                Resource.Success(
                    MovieDetails(
                        id = response.id,
                        title = response.title,
                        imDbRating = response.imDbRating,
                        year = response.year,
                        countries = response.countries,
                        genres = response.genres,
                        directors = response.directors,
                        writers = response.writers,
                        stars = response.stars,
                        plot = response.plot
                    )
                )
            }

            else -> Resource.Error("Ошибка сервера")
        }
    }

    override fun getCastList(movieId: String): Resource<CastInfo> {
        val response = networkClient.doRequest(CastInfoRequest(movieId))

        return when (response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")

            200 -> {
                (response as CastInfoResponse)
                Log.d("response: MovieRepository", "$response")
                Resource.Success(
                        CastInfo(
                            id= response.imDbId,
                            title = response.title,
                            fullTitle = response.fullTitle,
                            type = response.type,
                            year = response.year,
                            directors = CastShort(
                                job = "Director",
                                items = response.directors.items.map {
                                    CastShortItem(
                                        it.id,
                                        it.name,
                                        it.description
                                    )
                                }
                            ),
                            writers = CastShort(
                                job = "Writer",
                                items = response.writers.items.map {
                                    CastShortItem(
                                        it.id,
                                        it.name,
                                        it.description
                                    )
                                }
                            ),
                            actors = response.actors.map {
                                ActorShort(
                                    it.id,
                                    it.image,
                                    it.name,
                                    it.asCharacter
                                )
                            }
                        )


                )
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