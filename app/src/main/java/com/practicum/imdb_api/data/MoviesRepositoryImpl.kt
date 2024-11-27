package com.practicum.imdb_api.data

import android.util.Log
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.cast_members.CastShort
import com.practicum.imdb_api.data.Storage.LocalStorage
import com.practicum.imdb_api.data.converters.MovieDbConvertor
import com.practicum.imdb_api.data.db.AppDataBase
import com.practicum.imdb_api.data.dto.MovieDto
import com.practicum.imdb_api.data.dto.cast_request.CastInfoRequest
import com.practicum.imdb_api.data.dto.cast_request.CastInfoResponse
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsRequest
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsResponse
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchResponse
import com.practicum.imdb_api.data.dto.persons_search_request.PersonsSearchRequest
import com.practicum.imdb_api.data.dto.persons_search_request.PersonsSearchResponse
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.cast_members.ActorShort
import com.practicum.imdb_api.domain.models.cast_members.CastShortItem
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val appDataBase: AppDataBase,
    private val movieDbConvertor: MovieDbConvertor
) : MoviesRepository {

    override fun searchMovies(expression: String): Flow<Resource<List<Movie>>> = flow {

        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                val stored = localStorage.getSavedFavorites()
                with(response as MoviesSearchResponse) {
                    val data = results.map {
                        Movie(
                            it.id,
                            it.resultType,
                            it.image,
                            it.title,
                            it.description,
                            inFavorite = stored.contains(it.id)
                        )
                    }
                    saveMovies(results)
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }


    override fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MoviesDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                (response as MoviesDetailsResponse)
                val data = MovieDetails(
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
                emit(
                    Resource.Success(data)
                )
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }

    override fun getCastList(movieId: String): Flow<Resource<CastInfo>> = flow {

        val response = networkClient.doRequest(CastInfoRequest(movieId))

        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                (response as CastInfoResponse)
                Log.d("response: MovieRepository", "$response")
                emit(Resource.Success(
                    CastInfo(
                        id = response.imDbId,
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
                )
            }

            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }

    override fun searchPersons(expression: String): Flow<Resource<List<Person>>> = flow {
        val response = networkClient.doRequest(PersonsSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))
            200 -> {
                with(response as PersonsSearchResponse) {
                    val data = results.map {
                        Person(
                            it.id,
                            it.resultType,
                            it.image,
                            it.title,
                            it.description
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }

    private suspend fun saveMovies(movies: List<MovieDto>){
        val movieEntities = movies.map {
            movie -> movieDbConvertor.map(movie)
        }
        appDataBase.movieDao().insertMovies(movieEntities)
    }
}