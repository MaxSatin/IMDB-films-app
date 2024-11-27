package com.practicum.imdb_api.data.network

import com.practicum.imdb_api.data.dto.cast_request.CastInfoResponse
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsResponse
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchResponse
import com.practicum.imdb_api.data.dto.persons_search_request.PersonsSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDbApiService {
    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    suspend fun searchMovies(@Path("expression") expression: String): MoviesSearchResponse

    @GET("/en/API/SearchName/k_zcuw1ytf/{expression}")
    suspend fun searchPersons(@Path("expression") expression: String): PersonsSearchResponse

    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): MoviesDetailsResponse

    @GET("/en/API/FullCast/k_zcuw1ytf/{movie_id}")
    suspend fun getCastList(@Path("movie_id") movieId: String): CastInfoResponse

}